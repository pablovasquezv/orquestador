package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.liquidaciones.java.eapsrv.ejb.MonedaTO;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.PIngresarInstruccions;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ModalidadTransaccionTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.OrdenTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * @author Pablo
 *
 */

@Component
public class InstruccionPagoObjs {
    private ObjectMapper objectMapper = new ObjectMapper();
    private String ordenLiquidacionTO = "ordenLiquidacionTO";
    @Autowired
    private Utils utils;
    public InstruccionPagoObjs() {
        //sonar
    }
    /**
     * Función encargada de mapear el objeto InstruccionPagoInTO
     *
     * @param instruccion
     * @param objetoRespuesta
     * @return InstruccionPagoTO para la funcion InstruccionPagoInTO
     * @throws JsonProcessingException
     */
    public InstruccionPagoInTO crearInstruccionFondosMutuos(PIngresarInstruccions.PIngresarInstruccion instruccion, JSONObject objetoRespuesta) throws JsonProcessingException {
        InstruccionPagoInTO instruccionPagoInTO = new InstruccionPagoInTO();
        instruccionPagoInTO.setGuid("");
        InstruccionPagoTO instruccionPagoTO = new InstruccionPagoTO();
        MonedaTO moneda = new MonedaTO();
        ModalidadTransaccionTO modalidadTransaccionTO = new ModalidadTransaccionTO();
        OrdenTO ordenTO = utils.completaOrdenTO(objetoRespuesta.getJSONObject("ordenTO"));
        instruccionPagoTO.setNumeroCuentaCorriente(instruccion.getNumeroCuentaCorriente());
        instruccionPagoTO.setFechaHoraIngreso(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
        instruccionPagoTO.setMontoPago(instruccion.getMonto());
        instruccionPagoTO.setRutBancoOrigen(setBanco(instruccion.getBancoOrigen()));
        instruccionPagoTO.setRutBancoDeposito(setBanco(instruccion.getBancoDeposito()));
        instruccionPagoTO.setUsuarioIngresa(ordenTO.getUsuarioAutorizador());
        instruccionPagoTO.setCodigoModalidadPago(String.valueOf( instruccion.getCodigoModalidad().longValue()));
        modalidadTransaccionTO.setIdModalidadTransaccion(instruccion.getCodigoModalidad().longValue() );
        modalidadTransaccionTO.setDescripcionModalidadTransaccion(String.valueOf(instruccion.getBancoDeposito()));
        modalidadTransaccionTO.setEstadoLiquidacion(ordenTO.getEstadoOrden());
        instruccionPagoTO.setModalidadTransaccion(modalidadTransaccionTO);
        moneda.setCodigoMoneda(instruccion.getMoneda());
        instruccionPagoTO.setMonedaInstruccion(moneda);
        instruccionPagoTO.setOrdenTO(ordenTO);
        instruccionPagoInTO.setInstruccionPago(instruccionPagoTO);
        return instruccionPagoInTO;
    }
    /**
     * Función encargada de de modificación de RutBancoOrigen
     *
     * @param banco
     * @return String.valueOf(banco);
     */
    private String setBanco(int banco) {
        if (("0").equals(String.valueOf(banco))) {
            return null;
        }
        return String.valueOf(banco);
    }
    /**
     * Función encargada de mapear el objeto InstruccionPagoInTO para Moneda Extranjera
     *
     * @param instruccion
     * @param ingope
     * @param objetoRespuesta
     * @return InstruccionPagoInTO
     * @throws JsonProcessingException
     */
    public InstruccionPagoInTO crearInstruccionMonedaExtranjera(PIngresarInstruccions.PIngresarInstruccion instruccion,
                                                                Ingope ingope, JSONObject objetoRespuesta)
            throws JsonProcessingException {
        InstruccionPagoInTO instruccionPagoInTO = new InstruccionPagoInTO();
        InstruccionPagoTO instruccionPagoTO = new InstruccionPagoTO();
        MonedaTO moneda = new MonedaTO();
        ModalidadTransaccionTO modalidadTransaccionTO = new ModalidadTransaccionTO();
        //TODO completar los campos con el objeto instrucción
        instruccionPagoTO.setNumeroCuentaCorriente(instruccion.getNumeroCuentaCorriente());
        instruccionPagoTO.setFechaHoraIngreso(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
        instruccionPagoTO.setMontoPago(instruccion.getMonto());
        instruccionPagoTO.setRutBancoOrigen(setBanco(instruccion.getBancoOrigen()));
        instruccionPagoTO.setRutBancoDeposito(setBanco(instruccion.getBancoDeposito()));
        instruccionPagoTO.setUsuarioIngresa(String.valueOf(ingope.getUsuarioEjecutivoConectado()));
        instruccionPagoTO.setCodigoModalidadPago(String.valueOf(instruccion.getCodigoModalidad().longValue()));
        JSONObject ordenLiquidacion = new JSONObject(objetoRespuesta.getJSONObject(ordenLiquidacionTO).toString());
        ordenLiquidacion = utils.arreglarJson(ordenLiquidacion);
        instruccionPagoTO.setOrdenTO(utils.completaOrdenTO(ordenLiquidacion));
        instruccionPagoTO.setNumeroCuentaCorriente(instruccion.getNumeroCuentaCorriente());
        modalidadTransaccionTO.setIdModalidadTransaccion(instruccion.getCodigoModalidad().longValue());
        modalidadTransaccionTO.setDescripcionModalidadTransaccion(String.valueOf(instruccion.getBancoOrigen()));
        modalidadTransaccionTO.setEstadoLiquidacion(instruccionPagoTO.getOrdenTO().getEstadoOrden());
        instruccionPagoTO.setModalidadTransaccion(modalidadTransaccionTO);
        instruccionPagoTO.setUsuarioIngresa(ingope.getUsuarioEjecutivoConectado());
        // ver de donde proviene, si no existe registro en la tabla TL_CAJ por el rut del empleado, se asume que es CAJ-BCO
        instruccionPagoTO.setCodigoCaja("CAJ-BCO");//Va asociado a un empleado
        // se valida si viene cuenta corriente para determinar codigomodalidadpago
        if(instruccion.getNumeroCuentaCorriente()!=null){
            instruccionPagoTO.setCodigoModalidadPago("CGO-CTA");
        }
        moneda.setCodigoMoneda(instruccion.getMoneda());
        instruccionPagoTO.setMonedaInstruccion(moneda);
        instruccionPagoInTO.setInstruccionPago(instruccionPagoTO);
        instruccionPagoInTO.setGuid("");
        return instruccionPagoInTO;
    }
}
