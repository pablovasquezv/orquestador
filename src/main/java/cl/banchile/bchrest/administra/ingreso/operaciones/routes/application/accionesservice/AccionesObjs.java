package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice.model.OperacionAccionesModel;
import cl.banchile.go.ejb.CanalAtencionTO;
import cl.banchile.go.ejb.ClienteTO;
import cl.banchile.go.ejb.OperacionEnum;
import cl.banchile.go.ejb.ProductoEnum;
import cl.banchile.ingope.Ingope;
import org.springframework.beans.factory.annotation.Autowired;
import service.global.utilitario.banchile.cl._1.EmpleadoTO;

/**
 * @author Pablo
 *
 */

public class AccionesObjs {
    @Autowired
    private static OperacionAccionesModel requestOperacion;
    private AccionesObjs() {
        //sonar
    }
    /**
     * Función encargada de invocar a los metodos que mapearan el objeto OperacionAccionesModel
     *
     * @param objIngope
     * @return OperacionAccionesModel
     */
    public static OperacionAccionesModel crearObjetoAcciones(Ingope objIngope) {
        requestOperacion = new OperacionAccionesModel();
        crearDatosBase(objIngope);
        crearCanal(objIngope);
        crearCliente(objIngope);
        crearEjecutivo(objIngope);
        completarMontoYPrecio(objIngope);
        return requestOperacion;
    }
    /**
     * Método encargado de crear los datos bases en el objeto OperacionAccionesModel
     *
     * @param objIngope
     */
    private static void crearDatosBase(Ingope objIngope) {
        requestOperacion = new OperacionAccionesModel();
        requestOperacion.setCanalOperacion(objIngope.getCanal());
        requestOperacion.setIdTransaccion(String.valueOf(objIngope.getIdentificadorTransaccion()));
        requestOperacion.setFechaVigencia(objIngope.getOperacionAcciones().getFechaVigencia());
        requestOperacion.setNombreSerie(objIngope.getOperacionAcciones().getNombreSerie());
        requestOperacion.setRutCliente(String.valueOf(objIngope.getIdentificadorCliente()));
        requestOperacion.setSruCliente(Integer.valueOf(objIngope.getCuentaInversion()));
        requestOperacion.setUnidadesTransadas(objIngope.getUnidadesTransadas());
        requestOperacion.setIndicadorPrimeraEmision(objIngope.getOperacionAcciones().getIndicadorPrimeraEmision());
        requestOperacion.setNumeroCuentaCorriente(String.valueOf(objIngope.getOperacionAcciones().getNumeroCuentaCorriente()));
        requestOperacion.setTipoOperacion(objIngope.getOperacionAcciones().getTipoOperacionCorta());
        requestOperacion.setCondicionDeLiquidacion(objIngope.getOperacionAcciones().getCondicionDeLiquidacion());
        requestOperacion.setComision(objIngope.getOperacionAcciones().getComision());
        requestOperacion.setNumeroInformeRemate(objIngope.getOperacionAcciones().getNumeroInformeRemate());
        requestOperacion.setTipoOperacionEnum(OperacionEnum.valueOf(objIngope.getOperacion()));
        requestOperacion.setTipoProductoEnum(ProductoEnum.valueOf(objIngope.getProducto()));
    }
    /**
     * Método encargargado de crear el objeto CanalAtencionTO
     *
     * @param objIngope
     */
    private static void crearCanal(Ingope objIngope) {
        CanalAtencionTO canalAtencion = new CanalAtencionTO();
        canalAtencion.setIdentificadorCanalAtencion(objIngope.getIdentificadorCanalAtencion());
        requestOperacion.setCanalAtencionTO(canalAtencion);
    }
    /**
     * Método encargado de crear el objeto ClienteTO
     *
     * @param objIngope
     */
    private static void crearCliente(Ingope objIngope) {
        ClienteTO clienteTO = new ClienteTO();
        clienteTO.setRut(String.valueOf(objIngope.getIdentificadorCliente()));
        clienteTO.setSubRut(Integer.valueOf(objIngope.getCuentaInversion()));
        requestOperacion.setClienteTO(clienteTO);
    }
    /**
     * Método encargado de crear el EjecutivoConectadoTO, EjecutivoOperacionTO
     *
     * @param objIngope
     */
    private static void crearEjecutivo(Ingope objIngope) {
        EmpleadoTO ejecutivoConectado = new EmpleadoTO();
        ejecutivoConectado.setRutEmpleado(String.valueOf(objIngope.getRutEmpleadoEjecutivoConect()));
        ejecutivoConectado.setUsuario(objIngope.getUsuarioEjecutivoConectado());
        requestOperacion.setEjecutivoConectadoTO(ejecutivoConectado);
        //Método que crea el EjectutivoOperacion para el Objeto Ingope
        ejecutivoConectado.setRutEmpleado(String.valueOf(objIngope.getRutEmpleadoEjecutivoOperac()));
        requestOperacion.setEjecutivoOperacionTO(ejecutivoConectado);
    }
    /**
     * Método encargado de completar el mapeo de datos realacionados a los montos
     *
     * @param objIngope
     */
    private static void completarMontoYPrecio(Ingope objIngope) {
        requestOperacion.setIndicadorPrecioLimite(objIngope.getOperacionAcciones().getIndicadorPrecioLimite());
        requestOperacion.setMedioCobroPago(objIngope.getOperacionAcciones().getMedioCobroPago());
        requestOperacion.setMontoOperacion(objIngope.getMontoOperacionString());
        requestOperacion.setMontoValorizado(objIngope.getMontoOperacionString());
        requestOperacion.setPrecioLimite(objIngope.getOperacionAcciones().getMontoLimite());
        requestOperacion.setPrecioOrden(objIngope.getOperacionAcciones().getMontoOrden());
        requestOperacion.setCantidadOrden(objIngope.getOperacionAcciones().getCantidadOrden());
        requestOperacion.setIndicadorOrdenRemate(objIngope.getOperacionAcciones().getIndicadorOrdenRemate());
    }
}