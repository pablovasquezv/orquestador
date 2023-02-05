package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice;


import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice.model.OperacionAccionesModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.MonedaExtranjeraObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model.OperacionMonedaExtranjeraModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.impl.IngresoOperacionServiceImpl;
import cl.banchile.ingope.Ingope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pablo
 *
 */


class AccionesTest {
    private OperacionAccionesModel requestOperacionTest,requestOperacion;
    private ObjectMapper objectMapper;
    @BeforeEach
    void init(){
        requestOperacionTest= new OperacionAccionesModel();
        objectMapper= new ObjectMapper();
    }
    @Test
    void operacionAcciones() {
        try {
            requestOperacionTest.setCanalOperacion("WEB.BCH");
            Ingope objIngope = objectMapper.readValue(new File("src/test/resources/ingopeACC.json"), Ingope.class);
            requestOperacion = AccionesObjs.crearObjetoAcciones(objIngope);
            Assert.notNull(requestOperacion, "Objeto ACC es null");
            Assertions.assertEquals(requestOperacionTest.getCanalOperacion(), requestOperacion.getCanalOperacion());
            System.out.println("Objeto=> "+ requestOperacion.getCanalOperacion());
        } catch (IOException e) {
            System.out.println("Error"+ e.getMessage());
        }
    }
}