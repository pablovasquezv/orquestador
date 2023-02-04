package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model;
/**
 * @author Pablo
 *
 */

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ReinversionRequest {
    private int correlativoFondoMutuo;
    private int numeroOrden;
}