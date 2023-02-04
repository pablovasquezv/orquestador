package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services;

import org.springframework.http.ResponseEntity;
/**
 * @author Pablo
 *
 */

public interface IngresoOperacionService {
    ResponseEntity<?> operacion(String data);
}