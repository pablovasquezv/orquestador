package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.exception.ErrorException;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice.model.OperacionAccionesModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.EstadoSolicitudObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.ingope.Ingope;
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
import java.text.MessageFormat;

/**
 * @author Pablo
 *
 */

@Slf4j
@Component
public class AccionesServiceHandler {
    private String host;
    private String scheme;
    private int port;
    private String path;
    @Value("${constantes.mensajeExitoACC}")
    private String mensajeExitoACC;
    @Value("${constantes.mensajeErrorACC}")
    private String mensajeErrorACC;
    @Value("${constantes.mensajeErrorIngresoOperaciones}")
    private String mensajeErrorIngresoOperacion;
    @Autowired
    private EstadoSolicitudObjs estadoSolicitudObjs;
    private WebClient defaultWebClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OperacionTransaccion operacionTransaccion;
    /**
     * Constructor por defecto
     *
     * @param path             URL del servicio externo inyectada desde propertie
     * @param defaultWebClient WebClient por defecto, qye se intyecta desde el contexto de Spring
     */
    public AccionesServiceHandler(
            @Value("${bchrest-administra-ingreso-operaciones.acciones.path}") String path,
            @Value("${bchrest-administra-ingreso-operaciones.acciones.host}") String host,
            @Value("${bchrest-administra-ingreso-operaciones.acciones.scheme}") String scheme,
            @Value("${bchrest-administra-ingreso-operaciones.acciones.port}") int port,
            @Autowired WebClient defaultWebClient
    ) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.path = path;
        this.defaultWebClient = defaultWebClient;
    }
    /**
     * Método encargado de invocar al microservicio Acciones
     *
     * @param operacion
     * @param ingope
     * @return
     */
    public String operacion(OperacionAccionesModel operacion, Ingope ingope) {
        try {
            String rut = operacion.getClienteTO().getRut();
            String json = objectMapper.writeValueAsString(operacion);
            log.info("=======================================================================");
            log.info("Llamada a método para ingresar operaciones");
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
                                log.error("Ha ocurrido un error de tipo INTERNAL_SERVER_ERROR al intentar hacer un ingreso de operación para acciones");
                                operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                                        .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                                                MessageFormat.format(mensajeErrorACC, response),ingope));
                                return response.bodyToMono(ErrorException.class)
                                        .map(e -> new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorACC, mensajeErrorIngresoOperacion), e.getDetailedMessage()
                                                , rut));
                            }
                    )
                    .onStatus(
                            HttpStatus.BAD_REQUEST::equals,
                            response ->                            {
                                log.error("Ha ocurrido un error de tipo BAD_REQUEST al intentar hacer un ingreso de operación para acciones");
                                operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                                        .crearObjetoActualizarError(ingope.getIdentificadorTransaccion(),
                                                MessageFormat.format(mensajeErrorACC, response), ingope));
                                return response.bodyToMono(ErrorException.class).
                                        map(e -> new ErrorException(e.getCode(), MessageFormat.format(mensajeErrorACC, mensajeErrorIngresoOperacion), e.getDetailedMessage(),
                                                rut));
                            })
                    .bodyToMono(String.class)
                    .subscribe(s -> procesarOperacion(s, ingope));
        } catch (Exception e) {
            log.error("Error Handler Acciones Service" + e);
        }
        return "ok";
    }
    /**
     * Método encargado de invocar al actualiza sol_ope cuando es exitosa la llamada de la operacion
     *
     * @param s
     * @param ingope
     */
    private void procesarOperacion(String s, Ingope ingope) {
        log.info("=======================================================================");
        log.info("Procesando operacion de acciones con ID: " + ingope.getIdentificadorTransaccion() + " y el cliente: " + ingope.getIdentificadorCliente());
        log.info("=======================================================================");
        log.info("Objeto de salida EAP: " + s);
        log.info("=======================================================================");
        JSONObject jsonObject = new JSONObject(s);
        operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                .crearObjetoActualizar(ingope, jsonObject, false, mensajeExitoACC));
    }
}