/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.exception;

/**
 * @author Pablo
 *
 */

/**
 * Tipo de excepción de error en servicio esteno
 * Será lanzada en caso de error reportado por el servicio externo
 * este caso tiípicamente se presenta cuando existe un error controlado en el cliente Rest
 * el cual retorna una estructura estandarizada de error
 */
public class ExternalServiceException extends RuntimeException {
    /**
     * Constructor por defecto
     */
    public ExternalServiceException(){
        super();
    }
    public ExternalServiceException(String message){
        super(message);
    }
    public ExternalServiceException(Throwable ex){
        super(ex);
    }
    public ExternalServiceException(String message, Throwable ex){
        super(message, ex);
    }
}