package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice;

import cl.banchile.ingope.Ingope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author Pablo
 *
 */

@Slf4j
@Component
public class Acciones {
    @Autowired
    private AccionesServiceHandler accionesServiceHandler;
    /**
     * Método inicial que prepara el objeto de Acciones que sera enviado por AccionesServiceHandler al microservicio de
     * Acciones
     *
     * @param ingope
     */
    public void operacionAcciones(Ingope ingope) {
        accionesServiceHandler.operacion(AccionesObjs.crearObjetoAcciones(ingope), ingope);
    }
}