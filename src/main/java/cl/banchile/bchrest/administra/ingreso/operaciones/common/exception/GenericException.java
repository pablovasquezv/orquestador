/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.exception;

/**
 * @author Pablo
 *
 */

/**
 * Tipo de excepción genérica
 * será lanzada en casos de error inespecífico
 */
public class GenericException extends RuntimeException {
    /**
     * Constructor por defecto
     */
    public GenericException(){
        super();
    }
    public GenericException(String message){
        super(message);
    }
    public GenericException(Throwable ex){
        super(ex);
    }
    public GenericException(String message, Throwable ex){
        super(message, ex);
    }
}