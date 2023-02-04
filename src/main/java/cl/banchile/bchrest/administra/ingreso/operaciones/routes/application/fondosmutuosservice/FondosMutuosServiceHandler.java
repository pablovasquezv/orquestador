package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.exception.ErrorException;
import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model.OperacionFondoMutuoModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model.ReinversionRequest;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.EstadoSolicitudObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.BizagiObj;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.EjecutaBizagiWorkflowengine;
import cl.banchile.ingope.FirmarDocumentos;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.PIngresarInstruccions;
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

@Slf4j
@Component
public class FondosMutuosServiceHandler {
    private final String host;
    private final String scheme;
    private final int port;
    private final String path;
    private final String pathReinversion;
    private final WebClient defaultWebClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FondosMutuos fondosMutuos;
    @Autowired
    private OperacionTransaccion operacionTransaccion;
    @Autowired
    private EjecutaBizagiWorkflowengine ejecutaBizagiWorkflowengine;
    @Autowired
    private EstadoSolicitudObjs estadoSolicitudObjs;
    @Autowired
    private BizagiObj bizagiObj;
    @Autowired
    private Utils utils;
    @Value("${constantes.mensajeErrorFFMM}")
    private String mensajeErrorFFMM;
    @Value("${constantes.mensajeExitoFFMM}")
    private String mensajeExitoFFMM;
    @Value("${constantes.instruccionPago}")
    private String instruccionPago;
    @Value("${constantes.instruccionCobro}")
    private String instruccionCobro;
    @Value("${constantes.operacionRescate}")
    private String operacionRescate;
    @Value("${constantes.operacionAporte}")
    private String operacionAporte;
    @Value("${constantes.mensajeErrorIngresoOperaciones}")
    private String mensajeErrorIngresoOperacion;
    /**
     * Constructor por defecto
     *
     * @param path             URL del servicio externo inyectada desde propertie
     * @param defaultWebClient WebClient por defecto, qye se intyecta desde el contexto de Spring
     */
    public FondosMutuosServiceHandler(
            @Value("${bchrest-administra-ingreso-operaciones.fondos-mutuos.path}") String path,
            @Value("${bchrest-administra-ingreso-operaciones.fondos-mutuos.path-reinversion}") String pathReinversion,
            @Value("${bchrest-administra-ingreso-operaciones.fondos-mutuos.host}") String host,
            @Value("${bchrest-administra-ingreso-operaciones.fondos-mutuos.scheme}") String scheme,
            @Value("${bchrest-administra-ingreso-operaciones.fondos-mutuos.port}") int port,
            @Autowired WebClient defaultWebClient
    ) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.path = path;
        this.defaultWebClient = defaultWebClient;
        this.pathReinversion = pathReinversion;
    }
    /**
     * Método que envía una petición al microservicio administra-fondos-mutuos
     *
     * @param operacion
     * @param ingope
     */
    public void operacion(OperacionFondoMutuoModel operacion, Ingope ingope) {
        try {
            String rut = operacion.getClienteTO().getRut();
            String json = objectMapper.writeValueAsString(operacion);
            log.info("=======================================================================");
            log.info("Llamada a método para ingresar operación de fondos mutuos");
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
                                operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                                        .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                                                MessageFormat.format(mensajeErrorFFMM, response),ingope));
                                log.error("Ha ocurrido un error de tipo INTERNAL_SERVER_ERROR al intentar hacer un ingreso de operación para fondos mutuos");
                                return response.bodyToMono(ErrorException.class)
                                        .map(e -> new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorFFMM, mensajeErrorIngresoOperacion), e.getDetailedMessage(),
                                                rut));
                            }
                    )
                    .onStatus(
                            HttpStatus.BAD_REQUEST::equals,
                            response ->                            {
                                operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                                        .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                                                MessageFormat.format(mensajeErrorFFMM, response),ingope));
                                log.error("Ha ocurrido un error de tipo BAD_REQUEST al intentar hacer un ingreso de operación para fondos mutuos");
                                return response.bodyToMono(ErrorException.class).
                                        map(e -> new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorFFMM, mensajeErrorIngresoOperacion), e.getDetailedMessage(),
                                                rut));
                            })
                    .bodyToMono(String.class)
                    .subscribe(s -> procesarOperacion(s, ingope));
        } catch (Exception e) {
            log.error("Error al realizar un ingreso de operaciones => " + e);
        }
    }
    /**
     * Método que procesa el resultado del llamado al microservicio administra-fondos-mutuos, ejecuta las instrucciones de cobro o pago en caso de tener
     *
     * @param s,     String con formato Json, que corresponde a la respuesta del microservicio FFMM
     * @param ingope
     */
    private void procesarOperacion(String s, Ingope ingope) {
        log.info("=======================================================================");
        log.info("Procesando operacion de fondos mutuos con ID: " + ingope.getIdentificadorTransaccion() + " y el cliente: " + ingope.getIdentificadorCliente());
        log.info("=======================================================================");
        log.info("Objeto de salida EAP: " + s);
        log.info("=======================================================================");
        JSONObject jsonObject = new JSONObject(s);
        operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                .crearObjetoActualizar(ingope, jsonObject, false, mensajeExitoFFMM));
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
                    jsonObject.getJSONObject("ordenTO").getInt("numeroOperacionProducto"));
        }
    }
    /**
     * Método que procesa la llamada para realizar un ingreso de instrucción de cobro
     *
     * @param ingope,
     * @param jsonObject,
     * @param instruccion
     */
    private void procesarInstruccionPago(Ingope ingope, JSONObject jsonObject, PIngresarInstruccions.PIngresarInstruccion
            instruccion) {
        if (instruccion.getTipoInstruccion().toUpperCase().equals(instruccionPago)
            /* && ingope.getOperacion().toUpperCase().equals(operacionRescate)*/) {
            fondosMutuos.crearInstruccionCobro(ingope.getIdentificadorTransaccion(), instruccion, jsonObject, ingope);
            if (instruccion.getDatosReinversion() != null
                    && instruccion.getDatosReinversion().getOperacionFondosMutuos() != null) {
                reinversion(ingope, instruccion.getDatosReinversion().getPIngresarInstruccions());
            }
        }
    }
    /**
     * Método que procesa la llamada para realizar un ingreso de instrucción de pago
     *
     * @param ingope,
     * @param jsonObject,
     * @param instruccion
     */
    private void procesarInstruccionCobro(Ingope ingope, JSONObject jsonObject, PIngresarInstruccions.PIngresarInstruccion
            instruccion) {
        if (instruccion.getTipoInstruccion().toUpperCase().equals(instruccionCobro)
                && ingope.getOperacion().toUpperCase().equals(operacionAporte)) {
            fondosMutuos.crearInstruccionPago(ingope.getIdentificadorTransaccion(), instruccion, jsonObject,ingope);
        }
    }
    /**
     * Método que procesa la llamada para realizar una firma de documentos en Bizagi
     *
     * @param firmarDocumentos
     */
    public void bizagiProceso(FirmarDocumentos firmarDocumentos, long idTransaccion, int numeroOperacion) {
        //En construccion
        try {
            CreateCasesResponse.CreateCasesResult result = ejecutaBizagiWorkflowengine
                    .createCases(bizagiObj.crateCaseInfo(firmarDocumentos, idTransaccion, numeroOperacion));
            log.info("llamado exitoso =>{}", result);
        } catch (Exception e) {
            log.error("Ha ocurrido un error en FFMMHandler bizagiProceso",e);
        }
    }
    /**
     * Método que procesa la llamada para realizar una reinversión hacia fondos mutuos
     *
     * @param ingope,
     * @param instruccions
     */
    public void reinversion(Ingope ingope, PIngresarInstruccions instruccions) {
        Long ideGuiTrxHijo = utils.generarUUII();
        Long ideGuiTrxPadre = ingope.getIdentificadorTransaccion();
        operacionTransaccion.registrarTransaccion(estadoSolicitudObjs.crearObjetoInsertar(ideGuiTrxHijo));
        ingope.setIdentificadorTransaccion(ideGuiTrxHijo);
        OperacionFondoMutuoModel operacion = fondosMutuos.operacionFondosMutuosRNV(ingope, ideGuiTrxPadre);
        log.info("=======================================================================");
        log.info("Se procede a realizar una reinversión desde una operación " + ingope.getProducto() + " con transacción padre "
                + ideGuiTrxPadre + " hacia la nueva transacción hija " + ideGuiTrxHijo + " de tipo de operación FONDOS MUTUOS");
        log.info("=======================================================================");
        operacionReinversion(operacion, instruccions,ingope);
    }
    /**
     * Método que envía una petición al microservicio de Fondos Mutuos. Esto solo en el caso de ser una re-Inversión
     *
     * @param operacion
     * @param pIngresarInstruccions
     */
    public void operacionReinversion(OperacionFondoMutuoModel operacion, PIngresarInstruccions pIngresarInstruccions,
                                     Ingope ingope) {
        try {
            String rut = operacion.getClienteTO().getRut();
            String json = objectMapper.writeValueAsString(operacion);
            log.info("=======================================================================");
            log.info("Llamada a método para realizar una reinversión de fondos mutuos");
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
                                log.error("Ha ocurrido un error de tipo INTERNAL_SERVER_ERROR al intentar hacer un ingreso de reinversión para fondos mutuos");
                                return response.bodyToMono(ErrorException.class)
                                        .map(e -> ejecutaError(e, rut,ingope));
                            }
                    )
                    .onStatus(
                            HttpStatus.BAD_REQUEST::equals,
                            response -> {
                                log.error("Ha ocurrido un error de tipo BAD_REQUEST al intentar hacer un ingreso de reinversión para fondos mutuos");
                                return response.bodyToMono(ErrorException.class)
                                        .map(e -> ejecutaError(e, rut, ingope));
                            })
                    .bodyToMono(String.class)
                    .subscribe(s -> procesarOperacionReinversion(s, pIngresarInstruccions,ingope));
        } catch (Exception e) {
            log.debug("Error => " + e);
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
    private Throwable ejecutaError(ErrorException e, String rut,Ingope ingope) {
        operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(), MessageFormat.format(mensajeErrorFFMM, e.getMessage()),ingope));
        return new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorFFMM, mensajeErrorIngresoOperacion), e.getDetailedMessage(), rut);
    }
    /**
     * Método que procesa el resultado de una reinversión, ejecuta las instrucciones de cobro ó pago en caso de tener
     *
     * @param s,                    String con formato Json, que corresponde a la respuesta del microservicio FFMM
     * @param pIngresarInstruccions
     */
    private void procesarOperacionReinversion(String s, PIngresarInstruccions pIngresarInstruccions,Ingope ingope ) {
        log.info("=======================================================================");
        log.info("Procesando operacion reinversión de fondos mutuos con ID: " + ingope.getIdentificadorTransaccion() + " y el cliente: " + ingope.getIdentificadorCliente());
        log.info("=======================================================================");
        log.info("Objeto de salida EAP: " + s);
        log.info("=======================================================================");
        JSONObject jsonObject = new JSONObject(s);
        operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                .crearObjetoActualizar(ingope, jsonObject, true, mensajeExitoFFMM));
        jsonObject.getJSONObject("ordenTO").getInt("numeroOperacionProducto");
        ingope.getNumeroDeOrden();
        log.info("valores a enviar => SET"+jsonObject.getJSONObject("ordenTO").getInt("numeroOperacionProducto")
                + " WHERE => "+ingope.getNumeroDeOrden());
        // s -> ordenTO -> numeroOperacionProducto -> correlativo -> num_ope_pro
        // numeroOrden -> num_ord -> ingreso moneda ext
        ReinversionRequest req = new ReinversionRequest(jsonObject.getJSONObject("ordenTO").getInt("numeroOperacionProducto"),
                Integer.valueOf(ingope.getNumeroDeOrden()));
        actualizarCorrelativoReinversion(req,ingope);
    }
    /**
     * Método que envía una petición al microservicio administra-fondos-mutuos
     *
     * @param request
     * @param ingope
     */
    public void actualizarCorrelativoReinversion(ReinversionRequest request, Ingope ingope) {
        try {
            String json = objectMapper.writeValueAsString(request);
            log.info("=======================================================================");
            log.info("Llamada a método para actualizar campo cor_mov_fmu en la tabla tl_int_pag");
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
                                    .path(pathReinversion)
                                    .build()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(json))
                    .retrieve()
                    .onStatus(
                            HttpStatus.INTERNAL_SERVER_ERROR::equals,
                            response -> {
                                operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                                        .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                                                MessageFormat.format(mensajeErrorFFMM, response),ingope));
                                log.error("Ha ocurrido un error de tipo INTERNAL_SERVER_ERROR al intentar hacer un ingreso de operación para fondos mutuos");
                                return response.bodyToMono(ErrorException.class)
                                        .map(e -> new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorFFMM, mensajeErrorIngresoOperacion), e.getDetailedMessage()));
                            }
                    )
                    .onStatus(
                            HttpStatus.BAD_REQUEST::equals,
                            response ->                            {
                                operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                                        .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                                                MessageFormat.format(mensajeErrorFFMM, response),ingope));
                                log.error("Ha ocurrido un error de tipo BAD_REQUEST al intentar hacer un ingreso de operación para fondos mutuos");
                                return response.bodyToMono(ErrorException.class).
                                        map(e -> new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorFFMM, mensajeErrorIngresoOperacion), e.getDetailedMessage()));
                            })
                    .bodyToMono(String.class)
                    .subscribe(s -> procesarOperacionCorrelativo(s));
            ;
        } catch (Exception e) {
            log.error("Error al actualizar campo cor_mov_fmu en la tabla tl_int_pag => " + e);
        }
    }
    private void procesarOperacionCorrelativo(String s) {
        log.info("salida => "+s);
    }
}
