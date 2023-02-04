/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.exception;

/**
 * @author Pablo
 *
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Support {
    private ErrorException e;
    private String request;
}