/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.enums;

/**
 * @author Pablo
 *
 */
public enum ProductosEnum {
    FONDOSMUTUOS(1),
    ACCIONES(3),
    MONEDAEXTRANJERA(5);
    public final int idProducto;
    ProductosEnum(int idProducto) {
        this.idProducto = idProducto;
    }
}