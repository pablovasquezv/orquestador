/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase modelo con el cuerpo del mensaje de error como respuesta global a una excepción de un flujo proveniente de Rest Adapter
 * Lombok para la omisión de código redundante
 * Implementa patrón builder
 * Getters, setters, equals y hashcode con @Data
 * Constructor con y sin argumentos
 */

/**
 * @author Pablo
 *
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBody {
    private int code;
    private String message;
    private Integer status;
    private LocalDateTime timestamp;
}