package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external;

import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.InstruccionPagoOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenException_Exception;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenTO;

/**
 * @author Pablo
 */

public interface OperacionTransaccionOrden {
    OrdenTO actualizaEstadoOrdenLiquidacionCobro(OrdenTO inputOrdenTO, InstruccionPagoOutTO output) throws OrdenException_Exception;
}