package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model.OperacionFondoMutuoModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.EstadoSolicitudObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.InstruccionCobroObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.InstruccionPagoObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.OperacionTransaccionLiquidacion;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.PIngresarInstruccions;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionException_Exception;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoOutTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;

/**
 * @author Pablo
 *
 */

@Slf4j
@Component
public class FondosMutuos {
    @Autowired
    private FondosMutuosServiceHandler fondosMutuosHandler;
    @Autowired
    private OperacionTransaccionLiquidacion operacionTransaccionLiquidacion;
    private OperacionFondoMutuoModel requestOperacion;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OperacionTransaccion operacionTransaccion;
    @Autowired
    private EstadoSolicitudObjs estadoSolicitudObjs;
    @Autowired
    private FondosMutuosObjs fondosMutuosObjs;
    @Autowired
    private InstruccionCobroObjs instruccionCobroObjs;
    @Autowired
    private InstruccionPagoObjs instruccionPagoObjs;
    @Value("${constantes.mensajeErrorInstruccion}")
    private String mensajeErrorInstruccion;
    /**
     * Método principal para preparar objetos request del microservicio FFMM y envia el objeto a FondosMutuosServiceHandler
     *
     * @param objIngope
     */
    public void operacionFondosMutuos(Ingope objIngope) {
        requestOperacion = fondosMutuosObjs.crearObjetoFondosMutuos(objIngope);
        fondosMutuosHandler.operacion(requestOperacion, objIngope);
    }
    /**
     * Método principal para preparar objetos request del microservicio FFMM, cuando es una reinversión
     *
     * @param objIngope
     */
    public OperacionFondoMutuoModel operacionFondosMutuosRNV(Ingope objIngope,Long ideGuiTrxPadre) {
        return fondosMutuosObjs.crearObjetoFondosMutuosRNV(objIngope,ideGuiTrxPadre);
    }
    /**
     * Método que llama al EAP de movimientos para realizar una instruccion de cobro
     *
     * @param instruccion
     * @param objetoRespuesta
     */
    public void crearInstruccionCobro(Long ideGuiTrx,PIngresarInstruccions.PIngresarInstruccion instruccion,
                                      JSONObject objetoRespuesta, Ingope ingope) {
        try {
            InstruccionCobroInTO instruccionCobroInTO = instruccionCobroObjs
                    .crearInstruccionFondosMutuos(instruccion, objetoRespuesta);
            log.info("=======================================================================");
            log.info("Método para crear instrucción de cobro");
            log.info("=======================================================================");
            log.info("Objeto de entrada: " + objectMapper.writeValueAsString(instruccionCobroInTO));
            log.info("=======================================================================");
            InstruccionCobroOutTO result = operacionTransaccionLiquidacion.agregarInstruccionCobro(instruccionCobroInTO);
            log.info("Resultado EAP => " + objectMapper.writeValueAsString(result));
            log.info("=======================================================================");
        } catch (JsonProcessingException | InstruccionException_Exception e) {
            log.error("Ha ocurrido un error con la llamada al servicio de instrucción cobro => " + e);
            operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                    .crearObjetoActualizarError(ideGuiTrx,
                            MessageFormat.format(mensajeErrorInstruccion,"Cobro", e.getMessage()),ingope));
        }
    }
    /**
     * Método que llama al EAP de movimientos para realizar una instruccion de pago
     *
     * @param instruccion
     * @param objetoRespuesta
     */
    public void crearInstruccionPago(Long ideGuiTrx,PIngresarInstruccions.PIngresarInstruccion instruccion,
                                     JSONObject objetoRespuesta, Ingope ingope) {
        try {
            InstruccionPagoInTO instruccionPagoInTO = instruccionPagoObjs
                    .crearInstruccionFondosMutuos(instruccion, objetoRespuesta);
            log.info("=======================================================================");
            log.info("Método para crear instrucción de pago");
            log.info("=======================================================================");
            log.info("Objeto de entrada: " + objectMapper.writeValueAsString(instruccionPagoInTO));
            log.info("=======================================================================");
            InstruccionPagoOutTO instruccionPagoOutTO = operacionTransaccionLiquidacion
                    .agregarInstruccionPago(instruccionPagoInTO);
            log.info("Resultado EAP => " + objectMapper.writeValueAsString(instruccionPagoOutTO));
            log.info("=======================================================================");
        } catch (JsonProcessingException | InstruccionException_Exception e) {
            log.error("Ha ocurrido un error con la llamada al servicio de instruccion pago => " + e);
            operacionTransaccion.actualizarTransaccion(estadoSolicitudObjs
                    .crearObjetoActualizarError(ideGuiTrx,MessageFormat
                            .format(mensajeErrorInstruccion,"Pago", e.getMessage()),ingope));
        }
    }
}