package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model.OperacionMonedaExtranjeraModel;
import cl.banchile.go.ejb.OperacionEnum;
import cl.banchile.go.ejb.ProductoEnum;
import cl.banchile.go.ejb.CanalAtencionTO;
import cl.banchile.go.ejb.ClienteTO;
import cl.banchile.go.ejb.FechasOperacionTO;
import cl.banchile.go.ejb.MonedaTO;
import cl.banchile.ingope.Ingope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import service.global.utilitario.banchile.cl._1.EmpleadoTO;

/**
 * @author Pablo
 *
 */

@Component
public class MonedaExtranjeraObjs {
    private OperacionMonedaExtranjeraModel requestOperacion;
    @Value("${constantes.divisa}")
    private  String divisa;
    @Value("${constantes.monedaExtranjera}")
    private  String monedaExtranjera;
    @Autowired
    private Utils utils;
    /**
     * Función encargado de invocar a los metodos que mapearan el objeto OperacionMonedaExtranjeraModel
     * @param objIngope
     * @return OperacionMonedaExtranjeraModel
     */
    public  OperacionMonedaExtranjeraModel crearObjetoMonedaExtranjera(Ingope objIngope) {
        requestOperacion = new OperacionMonedaExtranjeraModel();
        completarCamposBase(objIngope);
        crearCanal(objIngope);
        crearCliente(objIngope);
        crearEjecutivoConectado(objIngope);
        crearEmpleado(objIngope);
        crearFechasOperacion(objIngope);
        crearMonedaPago(objIngope);
        crearMonedaCobro(objIngope);
        return requestOperacion;
    }
    /**
     * Método encargado de completar los cambos base del objeto OperacionMonedaExtranjeraModel
     *
     * @param objIngope
     */
    private  void completarCamposBase(Ingope objIngope) {
        requestOperacion.setIdTransaccion(String.valueOf(objIngope.getIdentificadorTransaccion()));
        requestOperacion.setMontoValorizado(objIngope.getMontoOperacionString());
        requestOperacion.setObservacion(objIngope.getObservacion());
        requestOperacion.setTipoOperacionEnum(OperacionEnum.fromValue(objIngope.getOperacion()));
        requestOperacion.setCodigoOficina(String.valueOf(objIngope.getOperacionMonedaExtranjera().getCodigoOficina()));
        requestOperacion.setUnidadesTransadas(objIngope.getUnidadesTransadas());
        requestOperacion.setMontoTotalPago(objIngope.getOperacionMonedaExtranjera().getMontoTotalPago());
        requestOperacion.setParidad(objIngope.getOperacionMonedaExtranjera().getParidad());
        requestOperacion.setPrecioCompra(objIngope.getOperacionMonedaExtranjera().getPrecioCompra());
        requestOperacion.setPrecioMedio(objIngope.getOperacionMonedaExtranjera().getPrecioMedio());
        requestOperacion.setCanalOperacion(objIngope.getCanal());
        requestOperacion.setPrecioVenta(utils.castBigDecimal( objIngope.getOperacionMonedaExtranjera().getPrecioVenta()));
        requestOperacion.setMontoComisionCalculada(utils.castBigDecimal(objIngope.getOperacionMonedaExtranjera().getMontoComisionCalculada()));
        requestOperacion.setMontoComisionRealCalculada(utils.castBigDecimal(objIngope.getOperacionMonedaExtranjera().getMontoComisionRealCalculada()));
    }
    /**
     * Método encargado de crear el objeto CanalAtencionTO
     *
     * @param objIngope
     */
    private  void crearCanal(Ingope objIngope) {
        CanalAtencionTO canalAtencion = new CanalAtencionTO();
        canalAtencion.setIdentificadorCanalAtencion(objIngope.getIdentificadorCanalAtencion());
        requestOperacion.setCanalAtencionTO(canalAtencion);
    }
    /**
     * Método encargado de crear el objeto ClienteTO
     *
     * @param objIngope
     */
    private  void crearCliente(Ingope objIngope) {
        ClienteTO clienteTO = new ClienteTO();
        clienteTO.setRut(String.valueOf(objIngope.getIdentificadorCliente()));
        clienteTO.setSubRut(Integer.valueOf(objIngope.getCuentaInversion()));
        requestOperacion.setClienteTO(clienteTO);
    }
    /**
     * Método encargado de crear el objeto EjecutivoConectadoTO
     *
     * @param objIngope
     */
    private  void crearEjecutivoConectado(Ingope objIngope) {
        EmpleadoTO ejecutivoConectado = new EmpleadoTO();
        ejecutivoConectado.setRutEmpleado(String.valueOf(objIngope.getRutEmpleadoEjecutivoConect()));
        ejecutivoConectado.setUsuario(objIngope.getUsuarioEjecutivoConectado());
        requestOperacion.setEjecutivoConectadoTO(ejecutivoConectado);
    }
    /**
     * Método encargado de crear el objeto EjecutivoOperacionTO
     *
     * @param objIngope
     */
    private  void crearEmpleado(Ingope objIngope) {
        EmpleadoTO ejecutivoOperacionTO = new EmpleadoTO();
        ejecutivoOperacionTO.setRutEmpleado(String.valueOf(objIngope.getRutEmpleadoEjecutivoConect()));
        requestOperacion.setEjecutivoOperacionTO(ejecutivoOperacionTO);
    }
    /**
     * Método encargado de crear el objeto FechasOperacionTO
     *
     * @param objIngope
     */
    private  void crearFechasOperacion(Ingope objIngope) {
        FechasOperacionTO fechaOperacionTO = new FechasOperacionTO();
        fechaOperacionTO.setFechaIngreso(utils.validaFechas(objIngope.getFechaIngreso()));
        fechaOperacionTO.setFechaSolicitud(utils.validaFechas(objIngope.getFechaSolicitud()));
        requestOperacion.setFechaOperacionTO(fechaOperacionTO);
    }
    /**
     * Método encargado de crear el objeto monedaPagoTO
     *
     * @param objIngope
     */
    private  void crearMonedaPago(Ingope objIngope) {
        MonedaTO monedaPagoTO = new MonedaTO();
        monedaPagoTO.setCodigoMoneda(objIngope.getOperacionMonedaExtranjera().getCodigoMonedaPago());
        monedaPagoTO.setCodigoIso(objIngope.getOperacionMonedaExtranjera().getCodigoIso());
        requestOperacion.setMonedaPagoTO(monedaPagoTO);
    }
    /**
     * Método encargado de crear el objeto monedaCobroTO
     *
     * @param objIngope
     */
    private  void crearMonedaCobro(Ingope objIngope) {
        MonedaTO monedaCobroTO = new MonedaTO();
        monedaCobroTO.setCodigoMoneda(objIngope.getOperacionMonedaExtranjera().getCodigoMonedaCobro());
        requestOperacion.setMonedaCobroTO(monedaCobroTO);
        if (objIngope.getProducto().toUpperCase().equals(divisa)) {
            requestOperacion.setTipoProductoEnum(ProductoEnum.fromValue(monedaExtranjera));
        } else {
            requestOperacion.setTipoProductoEnum(ProductoEnum.fromValue(objIngope.getProducto()));
        }
    }
}