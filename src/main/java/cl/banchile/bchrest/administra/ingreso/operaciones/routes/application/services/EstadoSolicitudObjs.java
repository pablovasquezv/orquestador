package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services;
/**
 * @author Pablo
 *
 */

import cl.banchile.bchrest.administra.ingreso.operaciones.common.enums.CanalesEnum;
import cl.banchile.bchrest.administra.ingreso.operaciones.common.enums.ProductosEnum;
import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.estado.solicitudoperacion.ejb.ActualizarTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.RegistrarTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.TransaccionTOAbs;
import cl.banchile.go.ejb.ProductoEnum;
import cl.banchile.ingope.Ingope;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EstadoSolicitudObjs {

    @Value("${constantes.exitoIngresoOperacion}")
    private Long exitoIngresoOperacion;

    @Value("${constantes.errorIngresoOperacion}")
    private Long errorIngresoOperacion;

    @Value("${constantes.errorIngresoOperacionInstruccion}")
    private Long errorIngresoOperacionInstruccion;

    @Value("${constantes.ingresarCaso}")
    private Long ingresarCaso;

    @Value("${constantes.operacionRescate}")
    private String operacionRescate;

    @Value("${constantes.operacionVenta}")
    private String operacionVenta;

    @Value("${constantes.divisa}")
    private  String divisa;

    @Value("${constantes.monedaExtranjera}")
    private  String monedaExtranjera;

    private String tipoProductoEnum = "tipoProductoEnum";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Utils utils;


    /**
     * Función encargada de obtener el codigo operacion
     *
     * @param- dato de entrada
     * @return- ato de salida
     */
    public Long obtenerCodigoOperacion(String operacion) {
        // VENTA -1 COMPRA 1 => Moneda Extranjera
        // RESCATE -1 APORTE 1 => Fondos Mutuos
        if (operacion.equals(operacionRescate) || operacion.equals(operacionVenta)) {
            return -1L;
        } else {
            return 1L;
        }
    }

    /**
     * Función encargada de obtener el codigo Producto
     *
     * @return int- dato de salida
     * @param- dato de entrada
     */
    public String obtenerCodigoProducto(String operacion) {
        String valor = "";
        switch (operacion) {
            case "APV":
                valor = "6";
                break;
            case "FONDOSMUTUOS":
                valor = "1";
                break;
            case "CARTERAACTIVA":
                valor = "2";
                break;
            case "ACCIONES":
                valor = "3";
                break;
            case "DIVISA":
                valor = "5";
                break;
            case "MONEDAEXTRANJERA":
                valor = "5";
                break;
            default:
                valor = "-1";
        }

        return valor;
    }

    /**
     * Función encargada de mapear el objeto ActualizaSolOpe para el caso de exito
     *

     * @param jsonObject-    dato de entrada
     * @return ActualizaSolOpe- dato de salida
     */
    public ActualizarTransaccionInTO crearObjetoActualizar(Ingope ingope, JSONObject jsonObject, Boolean rnv,
                                                           String mensaje) {
        ActualizarTransaccionInTO actualizarTransaccionInTO = new ActualizarTransaccionInTO();
        TransaccionTOAbs transaccion = new TransaccionTOAbs();

        ProductosEnum productosEnum= ProductosEnum.valueOf(jsonObject.get(tipoProductoEnum).toString());
        CanalesEnum canalesEnum = CanalesEnum.valueOf(jsonObject.get("canalOperacion").toString()
                .replace(".", ""));
        transaccion.setIdentificadorGUID(String.valueOf(ingope.getIdentificadorTransaccion()));

        if (!rnv) {
            switch(jsonObject.getString("tipoProductoEnum")){
                case "ACCIONES":
                    transaccion.setNumeroOperacion(utils.castLong(jsonObject.get("numeroOrden")));
                    break;
                case "FONDOSMUTUOS":
                    transaccion.setNumeroOrden(utils.castLong(jsonObject.getJSONObject("ordenTO").get("numeroOrden")));
                    break;
                case "MONEDAEXTRANJERA":
                    transaccion.setNumeroOrden(utils.castLong(jsonObject.getJSONObject("ordenLiquidacionTO").get("numeroOrden")));
                    break;
                default:
                    log.info("No se ingresó un tipo de producto válido");
                    break;
            }
        }
        transaccion.setIdentificadorCanalOperacion(utils.castLong(canalesEnum.idCanal));
        transaccion.setIdentificadorCanalAtencion(utils.castLong(jsonObject.getJSONObject("canalAtencionTO")
                .get("identificadorCanalAtencion").toString()));
        transaccion.setIdentificadorProducto((utils.castLong(productosEnum.idProducto)));
        transaccion.setDescripcionCanalAtencion(jsonObject.getString("canalOperacion").replace(".", ""));
        transaccion.setTipoOperacion(obtenerCodigoOperacion(jsonObject.getString(tipoProductoEnum)));
        transaccion.setMontoOperacion(utils.castBigDecimal(jsonObject.get("montoValorizado")));
        transaccion.setRutCliente(jsonObject.getJSONObject("clienteTO").getString("rut"));

        String producto = jsonObject.getString(tipoProductoEnum);

        if (producto.equals(ProductoEnum.FONDOSMUTUOS.name())) {
            transaccion.setNumeroOperacion(utils.castLong(jsonObject.getJSONObject("ordenTO").get("numeroOperacionProducto")));
            transaccion.setNumeroCuenta(utils.castLong(jsonObject.getJSONObject("cuentaFondoMutuoTO").get("numeroCuenta")));
            transaccion.setNemotecnicoProducto(jsonObject.getJSONObject("cuentaFondoMutuoTO").getJSONObject("fondoMutuo")
                    .getString("nemoFondoMutuo"));
        }

        if (producto.equals(ProductoEnum.MONEDAEXTRANJERA.name())) {
            transaccion.setNumeroOperacion(utils.castLong(jsonObject.getJSONObject("ordenLiquidacionTO")
                    .get("numeroOperacionProducto")));
        }
        if (producto.equals(ProductoEnum.ACCIONES.name())) {
            transaccion.setNumeroOperacion(utils.castLong(jsonObject.get("numeroOrden")));
            transaccion.setNumeroCuenta(null);
        }

        transaccion.setObservacionSolicitud(mensaje);
        transaccion.setEstadoSolicitud(
                exitoIngresoOperacion);

        actualizarTransaccionInTO.setTransaccionIn(transaccion);

        return actualizarTransaccionInTO;
    }


    /**
     * Función encargada de mapear el objeto ActualizaSolOpe para el caso de error
     *
     * @param idTransaccion- dato de entrada
     * @return ActualizaSolOpe- dato de salida
     */
    public ActualizarTransaccionInTO crearObjetoActualizarError(Long idTransaccion, String mensaje, Ingope ingope) {
        ActualizarTransaccionInTO actualizarTransaccionInTO = new ActualizarTransaccionInTO();
        TransaccionTOAbs transaccion = new TransaccionTOAbs();
        ProductosEnum productoEnum;

        if(ingope.getProducto().toUpperCase().equals(divisa)){
            productoEnum = ProductosEnum.valueOf(monedaExtranjera);
        }else{
            productoEnum = ProductosEnum.valueOf(ingope.getProducto());
        }

        CanalesEnum canalesEnum = CanalesEnum.valueOf(ingope.getCanal().replace(".", ""));

        transaccion.setIdentificadorGUID(String.valueOf(idTransaccion));
        transaccion.setObservacionSolicitud(mensaje);
        transaccion.setIdentificadorCanalAtencion(utils.castLong((ingope.getIdentificadorCanalAtencion())));
        transaccion.setIdentificadorCanalOperacion(utils.castLong(canalesEnum.idCanal));
        transaccion.setIdentificadorProducto(utils.castLong(productoEnum.idProducto));
        transaccion.setEstadoSolicitud(
                errorIngresoOperacion);
        actualizarTransaccionInTO.setTransaccionIn(transaccion);
        return actualizarTransaccionInTO;
    }

    /**
     * Función encargada de mapear el objeto ActualizaSolOpe para el caso de error
     *
     * @param idTransaccion- dato de entrada
     * @return ActualizaSolOpe- dato de salida
     */
    public ActualizarTransaccionInTO crearObjetoActualizarErrorInstruccion(Long idTransaccion, String mensaje, Ingope ingope) {
        ActualizarTransaccionInTO actualizarTransaccionInTO = new ActualizarTransaccionInTO();
        TransaccionTOAbs transaccion = new TransaccionTOAbs();

        ProductosEnum productoEnum;

        if(ingope.getProducto().toUpperCase().equals(divisa)){
            productoEnum = ProductosEnum.valueOf(monedaExtranjera);
        }else{
            productoEnum = ProductosEnum.valueOf(ingope.getProducto());
        }

        CanalesEnum canalesEnum = CanalesEnum.valueOf(ingope.getCanal().replace(".", ""));

        transaccion.setIdentificadorGUID(String.valueOf(idTransaccion));
        transaccion.setObservacionSolicitud(mensaje);
        transaccion.setIdentificadorCanalAtencion(utils.castLong((ingope.getIdentificadorCanalAtencion())));
        transaccion.setIdentificadorCanalOperacion(utils.castLong(canalesEnum.idCanal));
        transaccion.setIdentificadorProducto(utils.castLong(productoEnum.idProducto));
        transaccion.setEstadoSolicitud(
                errorIngresoOperacionInstruccion);
        actualizarTransaccionInTO.setTransaccionIn(transaccion);
        return actualizarTransaccionInTO;
    }

    /**
     * Función que crea el objeto necesario para insertar en la tabla tptv_est_sol_ope
     *
     * @param- ideGuiTrx- dato de entrada
     * @return- - salida- dato de salida
     */
    public RegistrarTransaccionInTO crearObjetoInsertar(Long ideGuiTrx) {
        RegistrarTransaccionInTO registrarTransaccionInTO = new RegistrarTransaccionInTO();
        TransaccionTOAbs transaccion = new TransaccionTOAbs();
        transaccion.setIdentificadorGUID(String.valueOf(ideGuiTrx));
        transaccion.setEstadoSolicitud(
                ingresarCaso);
        registrarTransaccionInTO.setTransaccionIn(transaccion);
        return registrarTransaccionInTO;
    }
}
