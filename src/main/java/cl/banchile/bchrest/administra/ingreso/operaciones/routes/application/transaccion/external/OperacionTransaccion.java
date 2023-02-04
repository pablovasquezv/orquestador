package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external;

import cl.banchile.estado.solicitudoperacion.ejb.ActualizarTransaccionOutTO;
import cl.banchile.estado.solicitudoperacion.ejb.ActualizarTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.ObtenerTransaccionOutTO;
import cl.banchile.estado.solicitudoperacion.ejb.ObtenerTransaccionInTO;
import cl.banchile.estado.solicitudoperacion.ejb.RegistrarTransaccionOutTO;
import cl.banchile.estado.solicitudoperacion.ejb.RegistrarTransaccionInTO;

/**
 * @author Pablo
 *
 */

public interface OperacionTransaccion {

    ActualizarTransaccionOutTO actualizarTransaccion(ActualizarTransaccionInTO operacion)  ;
    ObtenerTransaccionOutTO obtenerTransaccion(ObtenerTransaccionInTO operacion)  ;
    RegistrarTransaccionOutTO registrarTransaccion(RegistrarTransaccionInTO operacion)  ;

}