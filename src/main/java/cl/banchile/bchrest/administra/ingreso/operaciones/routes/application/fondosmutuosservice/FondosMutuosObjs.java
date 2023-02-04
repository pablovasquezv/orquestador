package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model.OperacionFondoMutuoModel;
import cl.banchile.go.ejb.OperacionEnum;
import cl.banchile.go.ejb.ProductoEnum;
import cl.banchile.go.ejb.CanalAtencionTO;
import cl.banchile.go.ejb.ClienteTO;
import cl.banchile.go.ejb.FechasOperacionTO;
import cl.banchile.go.ejb.FondoMutuoTO;
import cl.banchile.go.ejb.CuentaFondoMutuoTO;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.OperacionFondosMutuos;
import cl.banchile.ingope.PIngresarInstruccions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import service.global.utilitario.banchile.cl._1.EmpleadoTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Pablo
 *
 */

@Component
public class FondosMutuosObjs {


    private OperacionFondoMutuoModel requestOperacion;

    @Value("${constantes.usuarioInternet}")
    private String usuarioInternet;

    @Value("${constantes.instruccionPago}")
    private String instruccionPago;
    private int rutEjecutivoInternet = 800000602;

    @Autowired
    private Utils utils;


    /**
     * Función encargada de llenar el objeto OperacionFondoMutuoModel
     *
     * @param objIngope
     * @return OperacionFondoMutuoModel
     */
    public  OperacionFondoMutuoModel crearObjetoFondosMutuos(Ingope objIngope) {
        requestOperacion = new OperacionFondoMutuoModel();
        crearDatosBase(objIngope);
        crearCanal(objIngope);
        crearCliente(objIngope);
        crearEjecutivoConectado(objIngope);
        crearCuentaFondoMutuo(objIngope);
        crearFechaOperacioTO();

        return requestOperacion;
    }

    /**
     * Función encargado de crear el objeto OperacionFondoMutuoModel cuando contenga una reinversión
     *
     * @param objIngope
     * @return OperacionFondoMutuoModel
     */
    public OperacionFondoMutuoModel crearObjetoFondosMutuosRNV(Ingope objIngope, Long ideGuiTrxPadre) {
        requestOperacion = new OperacionFondoMutuoModel();
        PIngresarInstruccions.PIngresarInstruccion instruccion = objIngope.getPIngresarInstruccions().getPIngresarInstruccion().stream().filter(
                inst -> inst.getTipoInstruccion().toUpperCase().equals((instruccionPago))).findFirst().get();


        OperacionFondosMutuos operacionFondosMutuos = instruccion.getDatosReinversion().getOperacionFondosMutuos();
        String rut = String.valueOf(objIngope.getIdentificadorCliente());
        int subrut = objIngope.getCuentaInversion();
        String ejecutivo = objIngope.getUsuarioEjecutivoConectado();

        completarCamposRNV(objIngope, ideGuiTrxPadre);
        crearComplementos(instruccion, Long.valueOf(objIngope.getIdentificadorCanalAtencion()));
        camposReinversion(operacionFondosMutuos);
        cuentaFFMM(operacionFondosMutuos, rut, subrut);
        clienteEjecutivo(instruccion, rut, subrut, ejecutivo);

        return requestOperacion;
    }

    /**
     * Método encargado de crear datos base del ingreso de operación
     *
     * @param objIngope
     */
    private  void crearDatosBase(Ingope objIngope) {

        requestOperacion.setMontoValorizado(objIngope.getMontoOperacionString());
        requestOperacion.setObservacion(objIngope.getObservacion());
        requestOperacion.setTipoOperacionEnum(OperacionEnum.fromValue(objIngope.getOperacion()));
        requestOperacion.setTipoProductoEnum(ProductoEnum.fromValue(objIngope.getProducto()));
        requestOperacion.setIndicadorCuotaPeso(objIngope.getOperacionFondosMutuos().getIndicadorCuotaPeso());
        requestOperacion.setIndicadorRescateTotal(objIngope.getOperacionFondosMutuos().getIndicadorRescateTotal());
        requestOperacion.setTipoCurseRescate(objIngope.getOperacionFondosMutuos().getTipoCurseRescate());
        requestOperacion.setIdOrigenDeFondo(Long.valueOf(objIngope.getOperacionFondosMutuos().getIdOrigenFondo()));
        requestOperacion.setIndicador18Q(utils.completarIndicador18Q(objIngope.getOperacionFondosMutuos().getIndicador18Q()));
        requestOperacion.setUnidadesTransadas(objIngope.getUnidadesTransadas());//null
    }

    /**
     * Método encargado de crear el objeto CanalAtencionTO
     *
     * @param objIngope
     */
    private  void crearCanal(Ingope objIngope) {
        requestOperacion.setCanalOperacion(objIngope.getCanal());
        CanalAtencionTO canalAtencion = new CanalAtencionTO();
        canalAtencion.setIdentificadorCanalAtencion(objIngope.getIdentificadorCanalAtencion());
        requestOperacion.setCanalAtencionTO(canalAtencion);

    }

    /**
     * Método encargado de crear el objeto FechasOperacionTO, para el caso de reinversión debería cambiar la fecha
     *
     * @param
     */
    private  void crearFechaOperacioTO() {
        FechasOperacionTO fechaOperacionTO = new FechasOperacionTO();
        fechaOperacionTO.setFechaSolicitud(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
        fechaOperacionTO.setFechaIngreso(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
        requestOperacion.setFechaOperacionTO(fechaOperacionTO);
    }

    /**
     * Método encargado de crear el objeto ClienteTO, para el caso de reinversión no debería cambiar el cliente
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
     * Método encargado de crear los objetos EjecutivoConectadoTO, EjecutivoOperacionTO
     *
     * @param objIngope
     */
    private  void crearEjecutivoConectado(Ingope objIngope) {
        EmpleadoTO ejecutivoConectado = new EmpleadoTO();
        ejecutivoConectado.setRutEmpleado(String.valueOf(objIngope.getRutEmpleadoEjecutivoConect()));
        ejecutivoConectado.setUsuario(objIngope.getUsuarioEjecutivoConectado());
        requestOperacion.setEjecutivoConectadoTO(ejecutivoConectado);
        requestOperacion.setEjecutivoOperacionTO(ejecutivoConectado);

    }

    /**
     * Método encargado de crear el objeto CuentaFondoMutuoTO, FondoMutuoTO
     *
     * @param objIngope
     */
    private  void crearCuentaFondoMutuo(Ingope objIngope) {

        CuentaFondoMutuoTO cuentaFondoMutuoTO = new CuentaFondoMutuoTO();
        FondoMutuoTO fondoMutuo = new FondoMutuoTO();
        cuentaFondoMutuoTO.setCodigoPlanCuenta(String.valueOf(objIngope.getOperacionFondosMutuos().getCodigoPlanCuenta()));
        cuentaFondoMutuoTO.setBeneficioTributario(objIngope.getOperacionFondosMutuos().getBeneficioTributario());
        cuentaFondoMutuoTO.setRutCliente(String.valueOf(objIngope.getIdentificadorCliente()));
        cuentaFondoMutuoTO.setSubRutCliente(objIngope.getCuentaInversion());
        fondoMutuo.setNemoFondoMutuo(objIngope.getOperacionFondosMutuos().getNemoFondoMutuo());
        cuentaFondoMutuoTO.setFondoMutuo(fondoMutuo);
        cuentaFondoMutuoTO.setNemotecnicoFondo(objIngope.getOperacionFondosMutuos().getNemoFondoMutuo());

        cuentaFondoMutuoTO.setNumeroCuenta( objIngope.getOperacionFondosMutuos().getNumeroCuenta().intValue());//0
        requestOperacion.setCuentaFondoMutuoTO(cuentaFondoMutuoTO);
    }

    /**
     * Método que completa el mapeo de los campos del objeto Ingope RNV
     *
     * @param objIngope
     */
    private  void completarCamposRNV(Ingope objIngope, Long ideGuiTrxPadre) {
        // Debería venir un id nuevo para este ingreso, mientras se genera con el paso 1 de colección SOAP, ver como generar el id
        requestOperacion.setIdTransaccion(String.valueOf(objIngope.getIdentificadorTransaccion()));
        //requestOperacion.setIdTransaccion(null);
        requestOperacion.setObservacion(" IDGuiTrx Hijo " + objIngope.getIdentificadorTransaccion() + " de Padre " + ideGuiTrxPadre);
        // Al ser una reinversión, el tipo de operación debe ser un aporte ya que se reingresa monto a otro FFMM
        requestOperacion.setTipoOperacionEnum(OperacionEnum.APORTERNV);
        // Para este caso se asume que la reinversión es FFMM debido a que WEB.BCH solo tiene habilitado este caso
        requestOperacion.setTipoProductoEnum(ProductoEnum.FONDOSMUTUOS);
        // No viene este campo actualizado en el objeto de reinversion, se deja en 0.0
        requestOperacion.setUnidadesTransadas(BigDecimal.valueOf(0));

    }

    /**
     * Método que complementa el mapeo de objetos, crea monto, canalAtencion, canalOperacion
     *
     * @param instruccion
     */
    private  void crearComplementos(PIngresarInstruccions.PIngresarInstruccion instruccion,Long canalid) {
        requestOperacion.setMontoValorizado(instruccion.getMonto());
        requestOperacion.setCanalOperacion(instruccion.getCanalIngreso());
        CanalAtencionTO canalAtencion = new CanalAtencionTO();
        canalAtencion.setIdentificadorCanalAtencion(canalid);
        requestOperacion.setCanalAtencionTO(canalAtencion);

        if (("true").equals(instruccion.getIndicadorReinversion())) {
            FechasOperacionTO fechaOperacionTO = new FechasOperacionTO();
            fechaOperacionTO.setFechaSolicitud(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
            fechaOperacionTO.setFechaIngreso(utils.localDateTimeToXMLGregorianCalendar(LocalDateTime.now()));
            requestOperacion.setFechaOperacionTO(fechaOperacionTO);
        }
    }

    /**
     * Método que completa el mapeo de los campos del objeto Ingope RNV, con base en el objeto principal operacionFondosMutuos
     *
     * @param operacionFondosMutuos
     */
    private  void camposReinversion(OperacionFondosMutuos operacionFondosMutuos) {
        requestOperacion.setIndicadorCuotaPeso(operacionFondosMutuos.getIndicadorCuotaPeso());
        requestOperacion.setIndicadorRescateTotal(operacionFondosMutuos.getIndicadorRescateTotal());
        requestOperacion.setTipoCurseRescate(operacionFondosMutuos.getTipoCurseRescate());
        requestOperacion.setIdOrigenDeFondo(Long.valueOf(operacionFondosMutuos.getIdOrigenFondo()));
        requestOperacion.setIndicador18Q(utils.completarIndicador18Q(operacionFondosMutuos.getIndicador18Q()));
    }

    /**
     * Método que completa el mapeo de los campos del objeto Ingope RNV, CuentaFondoMutuoTO,FondoMutuoTO
     *
     * @param operacionFondosMutuos
     * @param rutCliente
     * @param subRutCliente
     */
    private  void cuentaFFMM(OperacionFondosMutuos operacionFondosMutuos, String rutCliente, int subRutCliente) {
        // Datos cuenta ffmm
        CuentaFondoMutuoTO cuentaFondoMutuoTO = new CuentaFondoMutuoTO();
        FondoMutuoTO fondoMutuo = new FondoMutuoTO();
        cuentaFondoMutuoTO.setCodigoPlanCuenta(String.valueOf(operacionFondosMutuos.getCodigoPlanCuenta()));
        cuentaFondoMutuoTO.setBeneficioTributario(operacionFondosMutuos.getBeneficioTributario());
        // Se mantienen datos de cliente
        cuentaFondoMutuoTO.setRutCliente(rutCliente);
        cuentaFondoMutuoTO.setSubRutCliente(subRutCliente);

        fondoMutuo.setNemoFondoMutuo(operacionFondosMutuos.getNemoFondoMutuo());
        cuentaFondoMutuoTO.setFondoMutuo(fondoMutuo);
        cuentaFondoMutuoTO.setNemotecnicoFondo(operacionFondosMutuos.getNemoFondoMutuo());
        cuentaFondoMutuoTO.setNumeroCuenta(utils.castInteger(operacionFondosMutuos.getNumeroCuenta()));

        requestOperacion.setCuentaFondoMutuoTO(cuentaFondoMutuoTO);
    }

    /**
     * Crea el ClienteTO, el ejecutivo Conectado, en caso de reinversión
     *
     * @param instruccion
     * @param rut
     * @param subrut
     */
    private  void clienteEjecutivo(PIngresarInstruccions.PIngresarInstruccion instruccion, String rut, int subrut,
                                   String ejecutivo) {
        // Para el caso de reinversión no debería cambiar el cliente
        ClienteTO clienteTO = new ClienteTO();
        clienteTO.setRut(rut);
        clienteTO.setSubRut(subrut);
        requestOperacion.setClienteTO(clienteTO);
        // Ejecutivo
        EmpleadoTO ejecutivoConectado = new EmpleadoTO();
        // Se podría asumir que el rutEmpleadoIngreso debería ser el rut 800000602 y tipo usuario INTERNET
        ejecutivoConectado.setRutEmpleado(String.valueOf(instruccion.getRutEmpleadoIngreso()));
        if(instruccion.getRutEmpleadoIngreso() == rutEjecutivoInternet){
            ejecutivoConectado.setUsuario(usuarioInternet);
        }else{
            ejecutivoConectado.setUsuario(ejecutivo);
        }

        requestOperacion.setEjecutivoConectadoTO(ejecutivoConectado);
        requestOperacion.setEjecutivoOperacionTO(ejecutivoConectado);
    }
}
