package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.liquidaciones.java.eapsrv.ejb.MonedaTO;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.OperacionFondosMutuos;
import cl.banchile.ingope.PIngresarInstruccions;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ModalidadTransaccionTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.OrdenTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * @author Pablo
 *
 */

@Component
public class InstruccionCobroObjs {
    @Value("${constantes.reinversionDolar}")
    private String reinversionDolar;
    private String orden = "ordenTO";
    private String ordenLiquidacionTO = "ordenLiquidacionTO";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Utils utils;
    public InstruccionCobroObjs() {
    }
    /**
     * Función encargada de mapear el objeto de InstruccionCobroInTO para FondosMutuos
     *
     * @param instruccion
     * @param objRespuesta
     * @return InstruccionCobroInTO para ser utilizado
     */
    public InstruccionCobroInTO crearInstruccionFondosMutuos(PIngresarInstruccions.PIngresarInstruccion instruccion, JSONObject objRespuesta) {
        InstruccionCobroInTO instruccionCobroInTO = new InstruccionCobroInTO();
        InstruccionCobroTO instruccionCobroTO = new InstruccionCobroTO();
        MonedaTO moneda = new MonedaTO();
        //
        instruccionCobroTO.setFechaIngresoPago(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
        instruccionCobroTO.setNumeroOrden(utils.castLong(objRespuesta.getJSONObject(orden).get("numeroOrden")));
        instruccionCobroTO.setCategoriaDeLiquidacion(instruccion.getCategoriaLiquidacion());
        instruccionCobroTO.setCodigoModalidad(String.valueOf(instruccion.getCodigoModalidad().longValue()));
        instruccionCobroTO.setRutEmpleadoIngreso(objRespuesta.getJSONObject(orden).getString("rutEmpleadoIngresa"));
        instruccionCobroTO.setMonto(instruccion.getMonto());
        instruccionCobroTO.setEstado(objRespuesta.getJSONObject(orden).getString("estadoOrden"));
        instruccionCobroTO.setRutBancoTransferenciaDeposito(String.valueOf(instruccion.getBancoDeposito()));
        instruccionCobroTO.setRutEmpleadoModifica(objRespuesta.getJSONObject(orden).getString("rutEmpleadoModifica"));
        //instruccionCobroTO.setCuentaCorriente(instruccion.getCuentaDeposito());
        // TODO revisar con Oscar el campo tendencia = M instruccionCobroTO.setTipoInstruccion(objRespuesta.getJSONObject("cuentaFondoMutuoTO").getJSONObject("fondoMutuo").getString("tendenciaDia"));
        ModalidadTransaccionTO modalidad = new ModalidadTransaccionTO();
        modalidad.setIdModalidadTransaccion( instruccion.getCodigoModalidad().longValue() );
        instruccionCobroTO.setModalidadTransaccion(modalidad);
        moneda.setCodigoMoneda(instruccion.getMoneda());
        instruccionCobroTO.setTipoInstruccion("M");
        instruccionCobroTO.setMonedaInstruccion(moneda);
        instruccionCobroInTO.setInstruccionCobro(instruccionCobroTO);
        instruccionCobroInTO.setGuid("");
        return instruccionCobroInTO;
    }
    /**
     * Función encargada de mapear InstruccionCobroInTO para MonedaExtranjera
     *
     * @param instruccion
     * @param ingope
     * @param objetoRespuesta
     * @return InstruccionCobroInTO
     */
    public InstruccionCobroInTO crearInstruccionMonedaExtranjera(PIngresarInstruccions.PIngresarInstruccion instruccion,
                                                                 Ingope ingope, JSONObject objetoRespuesta,
                                                                 OperacionFondosMutuos operacionFondosMutuos) throws JsonProcessingException {
        InstruccionCobroInTO instruccionCobroInTO = new InstruccionCobroInTO();
        InstruccionCobroTO instruccionCobroTO = new InstruccionCobroTO();
        ModalidadTransaccionTO modalidadTransaccionTO = new ModalidadTransaccionTO();
        MonedaTO monedaTO = new MonedaTO();
        JSONObject ordenLiquidacion = new JSONObject(objetoRespuesta.getJSONObject(ordenLiquidacionTO).toString());
        ordenLiquidacion = utils.arreglarJson(ordenLiquidacion);
        OrdenTO ordenTO = utils.completaOrdenTO(ordenLiquidacion);
        instruccionCobroTO.setFechaIngresoPago(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
        instruccionCobroTO.setCuentaCorriente(instruccion.getCuentaDeposito());
        instruccionCobroTO.setCodigoModalidad(String.valueOf(instruccion.getCodigoModalidad()));
        instruccionCobroTO.setRutEmpleadoIngreso(String.valueOf(instruccion.getRutEmpleadoIngreso()));
        instruccionCobroTO.setMonto(instruccion.getMonto());
        instruccionCobroTO.setEstado((objetoRespuesta.getJSONObject("ordenLiquidacionTO").getString("estadoOrden")));
        instruccionCobroTO.setRutBancoTransferenciaDeposito(String.valueOf(instruccion.getBancoDeposito()));
        instruccionCobroTO.setRutEmpleadoModifica(String.valueOf(ingope.getRutEmpleadoEjecutivoConect()));
        instruccionCobroTO.setRutEmpleadoModifica(String.valueOf(ingope.getRutEmpleadoEjecutivoOperac()));
        monedaTO.setCodigoMoneda(instruccion.getMoneda());
        instruccionCobroTO.setMonedaInstruccion(monedaTO);
        monedaTO.setCodigoMoneda(instruccion.getMoneda());
        instruccionCobroTO.setMonedaInstruccion(monedaTO);
        instruccionCobroTO.setPagoAgrupado(Boolean.parseBoolean(""));
        //ordenTO.setNumeroOrden(utils.castLong(objetoRespuesta.getJSONObject(ordenLiquidacionTO).get("numeroOrden")));
        instruccionCobroTO.setOrdenTO(ordenTO);
        instruccionCobroTO.setNumeroOrden(ordenTO.getNumeroOrden());
        //instruccionCobroTO.setCuentaCorriente(instruccion.getCuentaDeposito());
        instruccionCobroTO.setTipoInstruccion("M");
        modalidadTransaccionTO.setIdModalidadTransaccion(instruccion.getCodigoModalidad().longValue());
        instruccionCobroTO.setModalidadTransaccion(modalidadTransaccionTO);
        if (operacionFondosMutuos != null) {
            instruccionCobroTO.setCodigoOperacionLegacy(operacionFondosMutuos.getCodigoOperacion());
            instruccionCobroTO.setCodigoFondo(operacionFondosMutuos.getNemoFondoMutuo());
            instruccionCobroTO.setCodigoModalidad(reinversionDolar);
            instruccionCobroTO.setNumeroCuentaFondoMutuo(String.valueOf(operacionFondosMutuos.getNumeroCuenta()));
            ordenTO.setCodigoOperacion(operacionFondosMutuos.getCodigoOperacion());
        }
        instruccionCobroInTO.setInstruccionCobro(instruccionCobroTO);
        instruccionCobroInTO.setGuid("");
        return instruccionCobroInTO;
    }
}