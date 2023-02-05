package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external.impl;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.OperacionTransaccionLiquidacion;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionException_Exception;
import cl.banchile.liquidaciones.java.eapsrv.ejb.TransaccionLiquidacionServiceEjbService;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoInTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Pablo
 *
 */

@Service
public class OperacionTransaccionLiquidacionService implements OperacionTransaccionLiquidacion {
    private TransaccionLiquidacionServiceEjbService service;
    public OperacionTransaccionLiquidacionService(@Value("${service.transaccion-liquidacion.url}") String url) throws MalformedURLException{
        this.service = new TransaccionLiquidacionServiceEjbService(new URL(url));
    }
    @Override
    public InstruccionCobroOutTO agregarInstruccionCobro(InstruccionCobroInTO operacion) throws InstruccionException_Exception {
        return service.getTransaccionLiquidacionServiceEjbPort().agregarInstruccionCobro(operacion);
    }
    @Override
    public InstruccionPagoOutTO agregarInstruccionPago(InstruccionPagoInTO instruccionPagoInTO) throws InstruccionException_Exception {
        return service.getTransaccionLiquidacionServiceEjbPort().agregarInstruccionPago(instruccionPagoInTO);
    }
}