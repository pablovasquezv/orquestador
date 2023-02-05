package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external.impl;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.OperacionTransaccionOrden;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.InstruccionPagoOutTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenEjbService;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenException_Exception;
import cl.banchile.liquidaciones.java.eapsrv.ejb.ordenejb.OrdenTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Pablo
 *
 */

@Service
public class OperacionTransaccionOrdenService implements OperacionTransaccionOrden {
    private OrdenEjbService service;
    public OperacionTransaccionOrdenService(@Value("${service.transaccion-orden.url}") String url) throws MalformedURLException {
        this.service = new OrdenEjbService(new URL(url));
    }
    @Override
    public OrdenTO actualizaEstadoOrdenLiquidacionCobro(OrdenTO inputOrdenTO, InstruccionPagoOutTO output) throws OrdenException_Exception {
        return service.getOrdenEjbPort().actualizaEstadoOrdenLiquidacionCobro(inputOrdenTO, output);
    }
}