/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.IngresoOperacionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controlador rest ApplicationRestController
 * Permite la administración de ingreso de operaciones
 */

/**
 * @author Pablo
 *
 */

@RestController
@RequestMapping("/ingreso-operaciones/v1")
@Slf4j
public class ApplicationRestController {
    @Autowired
    private IngresoOperacionService ingresoOperacionService;
    @PostMapping("/operaciones")
    @ApiOperation(value = "Método para realizar un ingreso de operaciones")
    public ResponseEntity<?> ingresoOperacion(@RequestBody String data) {
        return ingresoOperacionService.operacion(data);
    }
}