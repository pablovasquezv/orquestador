/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.exception;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author Pablo
 *
 */
@Data
@NoArgsConstructor
@Slf4j
public class ErrorException extends RuntimeException {
    private Integer code;
    private String message;
    private String detailedMessage;
    private String request;
    public ErrorException(Integer code, String message, String detailedMessage, String id) {
        log.info("Ha ocurrido un Error llamando al servicio Codigo de Error " + code + " Mensaje " + message + "Detalle Mensaje" + detailedMessage + "Identificador Transaccion " + id);
    }
    public ErrorException(Integer code, String message, String detailedMessage) {
        log.info("Ha ocurrido un Error llamando al servicio Codigo de Error " + code + " Mensaje " + message + "Detalle Mensaje" + detailedMessage);
    }
}
