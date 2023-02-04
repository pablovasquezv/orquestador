package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.impl;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.OperacionTransaccion;
import cl.banchile.estado.solicitudoperacion.ejb.ActualizarTransaccionOutTO;
import cl.banchile.estado.solicitudoperacion.ejb.ActualizarTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.ObtenerTransaccionOutTO;
import cl.banchile.estado.solicitudoperacion.ejb.ObtenerTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.RegistrarTransaccionOutTO;
import cl.banchile.estado.solicitudoperacion.ejb.RegistrarTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.TransaccionServiceEjbService;
import cl.banchile.estado.solicitudoperacion.ejb.TransaccionException_Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Pablo
 *
 */

@Service
@Slf4j
public class OperacionTransaccionService implements OperacionTransaccion {

    private TransaccionServiceEjbService service;

    public OperacionTransaccionService(@Value("${service.transaccion-estadosolicitud.url}") String url) throws MalformedURLException {
        this.service = new TransaccionServiceEjbService(new URL(url));
    }

    @Override
    public ActualizarTransaccionOutTO actualizarTransaccion(ActualizarTransaccionInTO operacion)  {
        try {
            return service.getTransaccionServiceEjbPort().actualizarTransaccion(operacion);
        } catch (TransaccionException_Exception e) {
            log.error("Ha ocurrido un error actualizando el registro de la tabla tptv_est_sol_ope => ",e);
            return null;
        }
    }

    @Override
    public ObtenerTransaccionOutTO obtenerTransaccion(ObtenerTransaccionInTO operacion)  {
        try {
            return service.getTransaccionServiceEjbPort().obtenerTransaccion(operacion);
        } catch (TransaccionException_Exception e) {
            log.error("Ha ocurrido un error obteniendo el registro de la tabla tptv_est_sol_ope => ",e);
            return null;
        }
    }

    @Override
    public RegistrarTransaccionOutTO registrarTransaccion(RegistrarTransaccionInTO operacion)  {
        try {
            return service.getTransaccionServiceEjbPort().registrarTransaccion(operacion);
        } catch (TransaccionException_Exception e) {
            log.error("Ha ocurrido un error registrando en la tabla tptv_est_sol_ope => ",e);
            return null;
        }
    }

}