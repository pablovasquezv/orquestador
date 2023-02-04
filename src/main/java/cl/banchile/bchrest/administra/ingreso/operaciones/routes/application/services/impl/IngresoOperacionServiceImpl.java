package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.impl;

import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice.Acciones;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.FondosMutuos;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.MonedaExtranjera;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.IngresoOperacionService;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.ingope.Ingope;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Pablo
 *
 */

@Slf4j
@Service
public class IngresoOperacionServiceImpl implements IngresoOperacionService {
    @Autowired
    private FondosMutuos fondosMutuos;
    @Autowired
    private Acciones acciones;
    @Autowired
    private MonedaExtranjera monedaExtranjera;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OperacionTransaccion operacionTransaccion;
    @Autowired
    private Utils utils;
    /**
     * Método encargado de recibir la data y comenzar a procesarla para luego ser enviada a uno de los 3 microservicios ACC, FFMM, MEX
     *
     * @param data
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> operacion(String data) {
        try {
            Ingope objIngope = utils.mapeoIngope(new JSONObject(data));
            switch (prepararProducto(objIngope)) {
                case "FFMM":
                    log.info("Se realizará un ingreso de operaciones para fondos mutuos");
                    fondosMutuos.operacionFondosMutuos(objIngope);
                    break;
                case "MEX":
                    log.info("Se realizará un ingreso de operaciones para moneda extranjera");
                    monedaExtranjera.operacionMonedaExtranjera(objIngope);
                    break;
                case "ACC":
                    log.info("Se realizará un ingreso de operaciones para acciones");
                    acciones.operacionAcciones(objIngope);
                    break;
                default:
                    log.error("No se ingresó un producto permitido");
                    return new ResponseEntity<>("Opcion no permitida", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("llamado exitoso", HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Ha ocurrido un error => " + e);
            return new ResponseEntity<>("Ha ocurrido un error =>" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("Saliendo de operacion");
        }
    }
    /**
     * Función que indica que flujo se debe tomar dependiendo del @param de entrada
     *
     * @param ingope
     * @return un string indicando el tipo de producto
     */
    public String prepararProducto(Ingope ingope) {
        String producto = "";
        if (ingope.getOperacionFondosMutuos() != null) {
            producto = "FFMM";
        }
        if (ingope.getOperacionMonedaExtranjera() != null) {
            producto = "MEX";
        }
        if (ingope.getOperacionAcciones() != null) {
            producto = "ACC";
        }
        return producto;
    }
}