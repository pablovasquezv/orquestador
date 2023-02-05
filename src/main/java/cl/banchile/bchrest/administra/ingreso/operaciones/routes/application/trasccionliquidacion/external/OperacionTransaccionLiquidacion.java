package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external;

import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionException_Exception;

/**
 * @author Pablo
 *
 */

public interface OperacionTransaccionLiquidacion {
    InstruccionCobroOutTO agregarInstruccionCobro(InstruccionCobroInTO operacion) throws InstruccionException_Exception;
    InstruccionPagoOutTO agregarInstruccionPago(InstruccionPagoInTO instruccionPagoInTO) throws InstruccionException_Exception;
}
