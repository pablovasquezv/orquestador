package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.EstadoSolicitudObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.InstruccionCobroObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.InstruccionPagoObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.OperacionTransaccionLiquidacion;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.OperacionTransaccionOrden;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.OperacionFondosMutuos;
import cl.banchile.ingope.PIngresarInstruccions;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionException_Exception;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoOutTO;

import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenException_Exception;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pablo
 *
 */

@Slf4j
@Component
public class MonedaExtranjera {

    @Autowired
    private MonedaExtranjeraServiceHandler monedaExtranjeraServiceHandler;

    @Autowired
    private OperacionTransaccionLiquidacion operacionTransaccionLiquidacion;

    @Autowired
    private OperacionTransaccionOrden operacionTransaccionOrden;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperacionTransaccion operacionTransaccion;

    @Autowired
    private EstadoSolicitudObjs estadoSolicitudObjs;

    @Autowired
    private MonedaExtranjeraObjs monedaExtranjeraObjs;

    @Autowired
    private InstruccionCobroObjs instruccionCobroObjs;

    @Autowired
    private Utils utils;

    @Autowired
    private InstruccionPagoObjs instruccionPagoObjs;

    @Value("${constantes.mensajeErrorInstruccion}")
    private String mensajeErrorInstruccion;

    @Value("${modalidades.listanegra}")
    private String[] listaNegra;


    /**
     * Método inicial que prepara el objeto de Moneda Extranjera que sera enviado por MonedaExtranjeraServiceHandler
     * al microservicio de Moneda Extranjera
     *
     * @param ingope - dato de entrada
     */
    public void operacionMonedaExtranjera(Ingope ingope) {
        monedaExtranjeraServiceHandler.operacion(monedaExtranjeraObjs.crearObjetoMonedaExtranjera(ingope), ingope);
    }


    /**
     * Método que llama al EAP de movimientos para realizar una instruccion de cobro
     *
     * @param instruccion
     * @param ingope
     * @param objetoRespuesta
     * @param fondosMutuos
     */
    public void crearInstruccionCobro(PIngresarInstruccions.PIngresarInstruccion instruccion, Ingope ingope,
                                      JSONObject objetoRespuesta, OperacionFondosMutuos fondosMutuos) {
        try {
            InstruccionCobroInTO instruccionCobroInTO = instruccionCobroObjs
                    .crearInstruccionMonedaExtranjera(instruccion, ingope, objetoRespuesta, fondosMutuos);

            log.info("=======================================================================");
            log.info("Método para crear instrucción de cobro");
            log.info("=======================================================================");
            log.info("Objeto de entrada: " + objectMapper.writeValueAsString(instruccionCobroInTO));
            log.info("=======================================================================");

            InstruccionCobroOutTO result = operacionTransaccionLiquidacion.agregarInstruccionCobro(instruccionCobroInTO);

            log.info("Resultado EAP => " + objectMapper.writeValueAsString(result));
            log.info("=======================================================================");

        } catch (JsonProcessingException | InstruccionException_Exception e) {
            log.error("Ha ocurrido un error con la llamada al servicio de instrucción Cobro " + e);
            operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                    .crearObjetoActualizarErrorInstruccion(ingope.getIdentificadorTransaccion(),
                            MessageFormat.format(mensajeErrorInstruccion, "Cobro", e.getMessage()), ingope));
        }
    }

    /**
     * Método que llama al EAP de movimientos para realizar una instruccion de cobro
     *
     * @param instruccion
     * @param ingope
     * @param objetoRespuesta
     */
    public void crearInstruccionPago(PIngresarInstruccions.PIngresarInstruccion instruccion, Ingope ingope,
                                     JSONObject objetoRespuesta) {
        try {
            InstruccionPagoInTO instruccionPagoInTO = instruccionPagoObjs
                    .crearInstruccionMonedaExtranjera(instruccion, ingope, objetoRespuesta);
            log.info("=======================================================================");
            log.info("Método para crear instrucción de pago");
            log.info("=======================================================================");
            log.info("Objeto de entrada: " + objectMapper.writeValueAsString(instruccionPagoInTO));
            log.info("=======================================================================");

            InstruccionPagoOutTO instruccionPagoOutTO = operacionTransaccionLiquidacion
                    .agregarInstruccionPago(instruccionPagoInTO);

            log.info("Resultado EAP => " + objectMapper.writeValueAsString(instruccionPagoOutTO));
            log.info("=======================================================================");

            List<String> modalidadesNoPermitidas = Arrays.asList(listaNegra);
            if (!modalidadesNoPermitidas.contains(utils.castString(instruccion.getCodigoModalidad().intValue()))) {
                log.info("Llama a servicio para hacer la liquidación de la orden de pago");
                log.info("=======================================================================");
                cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.InstruccionPagoOutTO pago =
                        objectMapper.readValue(objectMapper.writeValueAsString(instruccionPagoOutTO), cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.InstruccionPagoOutTO.class);
                OrdenTO orden = objectMapper.readValue(objectMapper.writeValueAsString(instruccionPagoOutTO.getInstruccionPago().getOrdenTO()),
                        cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenTO.class);

                OrdenTO result2 = operacionTransaccionOrden.actualizaEstadoOrdenLiquidacionCobro(orden, pago);
                log.info("Resultado OrdenTO => " + objectMapper.writeValueAsString(result2));
                log.info("=======================================================================");

            }

        } catch (JsonProcessingException | InstruccionException_Exception | OrdenException_Exception e) {
            log.error("Ha ocurrido un error con la llamada al servicio de instrucción Pago => " + e);
            operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                    .crearObjetoActualizarErrorInstruccion(ingope.getIdentificadorTransaccion(),
                            MessageFormat.format(mensajeErrorInstruccion, "Pago", e.getMessage()), ingope));
        }
    }
}
