package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.exception.ErrorException;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.FondosMutuos;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.FondosMutuosServiceHandler;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model.OperacionMonedaExtranjeraModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.EstadoSolicitudObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.BizagiObj;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.EjecutaBizagiWorkflowengine;
import cl.banchile.ingope.FirmarDocumentos;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.PIngresarInstruccions;
import cl.banchile.ingope.OperacionFondosMutuos;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.tempuri.CreateCasesResponse;
import java.text.MessageFormat;

/**
 * @author Pablo
 *
 */

@Component
@Slf4j
public class MonedaExtranjeraServiceHandler {
    private final String host;
    private final String scheme;
    private final int port;
    private final String path;
    private final WebClient defaultWebClient;
    @Value("${constantes.mensajeErrorMEX}")
    private String mensajeErrorMEX;
    @Value("${constantes.mensajeExitoMEX}")
    private String mensajeExitoMEX;
    @Value("${constantes.instruccionPago}")
    private String instruccionPago;
    @Value("${constantes.instruccionCobro}")
    private String instruccionCobro;
    @Value("${constantes.mensajeErrorIngresoOperaciones}")
    private String mensajeErrorIngresoOperacion;
    @Autowired
    private BizagiObj bizagiObj;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MonedaExtranjera monedaExtranjera;
    @Autowired
    private OperacionTransaccion operacionTransaccion;
    @Autowired
    private EjecutaBizagiWorkflowengine ejecutaBizagiWorkflowengine;
    @Autowired
    private FondosMutuos fondosMutuos;
    @Autowired
    private FondosMutuosServiceHandler fondosMutuosHandler;
    @Autowired
    private EstadoSolicitudObjs estadoSolicitudObjs;
    /**
     * Constructor por defecto
     *
     * @param path             URL del servicio externo inyectada desde propertie
     * @param defaultWebClient WebClient por defecto, qye se intyecta desde el contexto de Spring
     */
    public MonedaExtranjeraServiceHandler(
            @Value("${bchrest-administra-ingreso-operaciones.moneda-extranjera.path}") String path,
            @Value("${bchrest-administra-ingreso-operaciones.moneda-extranjera.host}") String host,
            @Value("${bchrest-administra-ingreso-operaciones.moneda-extranjera.scheme}") String scheme,
            @Value("${bchrest-administra-ingreso-operaciones.moneda-extranjera.port}") int port,
            @Autowired WebClient defaultWebClient
    ) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.path = path;
        this.defaultWebClient = defaultWebClient;
    }
    /**
     * Método que envía una petición al microservicio administra-moneda-extranjera
     *
     * @param operacion
     * @param ingope
     */
    public void operacion(OperacionMonedaExtranjeraModel operacion, Ingope ingope) {
        try {
            String rut = operacion.getClienteTO().getRut();
            String json = objectMapper.writeValueAsString(operacion);
            log.info("=======================================================================");
            log.info("Llamada a método para ingresar operación de moneda extranjera");
            log.info("=======================================================================");
            log.info("Objeto de entrada: " + json);
            log.info("=======================================================================");
            defaultWebClient
                    .post()
                    .uri(
                            uriBuilder -> uriBuilder
                                    .scheme(scheme)
                                    .host(host)
                                    .port(port)
                                    .path(path)
                                    .build()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(json))
                    .retrieve()
                    .onStatus(
                            HttpStatus.INTERNAL_SERVER_ERROR::equals,
                            response -> {
                                log.error("Ha ocurrido un error de tipo INTERNAL_SERVER_ERROR al intentar hacer un ingreso de operación para moneda extranjera");
                                return response.bodyToMono(ErrorException.class)
                                        .map(e -> ejecutaError(e, rut, ingope));
                            }
                    )
                    .onStatus(
                            HttpStatus.BAD_REQUEST::equals,
                            response -> {
                                log.error("Ha ocurrido un error de tipo BAD_REQUEST al intentar hacer un ingreso de operación para moneda extranjera");
                                return response.bodyToMono(ErrorException.class).
                                        map(e -> ejecutaError(e, rut, ingope));
                            }
                    )
                    .bodyToMono(String.class)
                    .subscribe(s -> {
                        procesarOperacion(s, ingope);
                    });
        } catch (Exception e) {
            log.error("Error al realizar un ingreso de operaciones => " + e);
        }
    }
    /**
     * Función encargado de invocar al actualiza sol_ope cuando ocurrió un error en la llamada de la operación
     *
     * @param e
     * @param rut
     * @param ingope
     * @return ErrorException
     */
    private Throwable ejecutaError(ErrorException e, String rut, Ingope ingope) {
        operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs.
                crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                        MessageFormat.format(mensajeErrorMEX, e.getMessage()), ingope));
        //return new ErrorException(e.getCode(), e.getMessage(), e.getDetailedMessage(), rut);
        return new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorMEX, mensajeErrorIngresoOperacion), e.getDetailedMessage(), rut);
    }
    /**
     * Método que procesa el resultado del llamado al microservicio administra-moneda-extranjera, ejecuta las instrucciones de cobro o pago en caso de tener
     *
     * @param s
     * @param ingope
     */
    private void procesarOperacion(String s, Ingope ingope) {
        log.info("=======================================================================");
        log.info("Procesando operacion de moneda extranjera con ID: " + ingope.getIdentificadorTransaccion() + " y el cliente: " + ingope.getIdentificadorCliente());
        log.info("=======================================================================");
        log.info("Objeto de salida EAP: " + s);
        log.info("=======================================================================");
        JSONObject jsonObject = new JSONObject(s);
        ingope.setNumeroDeOrden(String.valueOf(jsonObject.getInt("numeroOrden")));
        operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs.
                crearObjetoActualizar(ingope, jsonObject, false, mensajeExitoMEX));
        if (ingope.getPIngresarInstruccions() != null) {
            for (PIngresarInstruccions.PIngresarInstruccion instruccion :
                    ingope.getPIngresarInstruccions().getPIngresarInstruccion()) {
                if (instruccion.getTipoInstruccion().toUpperCase().equals(instruccionPago)) {
                    procesarInstruccionPago(ingope, jsonObject, instruccion);
                } else {
                    procesarInstruccionCobro(ingope, jsonObject, instruccion);
                }
            }
        }
        if (ingope.getFirmarDocumentos() != null) {
            bizagiProceso(ingope.getFirmarDocumentos(), ingope.getIdentificadorTransaccion(),
                    jsonObject.getJSONObject("ordenLiquidacionTO").getInt("numeroOperacionProducto"));
        }
    }
    /**
     * Método que procesa la llamada para realizar un ingreso de instrucción de cobro
     *
     * @param ingope,
     * @param jsonObject,
     * @param pIngresarInstruccion
     */
    private void procesarInstruccionPago(Ingope ingope, JSONObject jsonObject, PIngresarInstruccions.PIngresarInstruccion
            pIngresarInstruccion) {
        log.info("=======================================================================");
        log.info("Entra a la instrucción de pago de la request inicial para procesar una instrucción de cobro");
        log.info("=======================================================================");
        OperacionFondosMutuos resul = null;
        if (pIngresarInstruccion.getDatosReinversion() != null) {
            resul = pIngresarInstruccion.getDatosReinversion().getOperacionFondosMutuos();
        }
        monedaExtranjera.crearInstruccionCobro(pIngresarInstruccion, ingope, jsonObject, resul);
        if (pIngresarInstruccion.getDatosReinversion() != null && resul != null) {
            fondosMutuosHandler.reinversion(ingope, pIngresarInstruccion.getDatosReinversion().getPIngresarInstruccions());
        }
    }
    /**
     * Método que procesa la llamada para realizar un ingreso de instrucción de pago
     *
     * @param ingope,
     * @param jsonObject,
     * @param pIngresarInstruccion
     */
    private void procesarInstruccionCobro(Ingope ingope, JSONObject jsonObject, PIngresarInstruccions.PIngresarInstruccion
            pIngresarInstruccion) {
        log.info("=======================================================================");
        log.info("Entra a la instrucción de cobro de la request inicial para procesar una instrucción de pago");
        log.info("=======================================================================");
        if (pIngresarInstruccion.getTipoInstruccion().toUpperCase().equals(instruccionCobro)) {
            monedaExtranjera.crearInstruccionPago(pIngresarInstruccion, ingope, jsonObject);
        }
    }
    /**
     * Método que procesa la llamada para realizar una firma de documentos en Bizagi
     *
     * @param firmarDocumentos
     */
    public void bizagiProceso(FirmarDocumentos firmarDocumentos, Long idTransaccion, int numeroOperacion) {
        CreateCasesResponse.CreateCasesResult result = ejecutaBizagiWorkflowengine.
                createCases(bizagiObj.crateCaseInfo(firmarDocumentos, idTransaccion, numeroOperacion));
        try {
            log.info("llamado exitoso a Bizagi => {}", objectMapper.writeValueAsString(result.getContent()));
        } catch (Exception e) {
            log.error("Ha ocurrido un error en MonedaHandler bizagiProceso", e);
        }
    }
}
