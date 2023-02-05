package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Pablo
 *
 */

@SpringBootTest
class EstadoSolicitudObjsTest {
    @Autowired
    private EstadoSolicitudObjs estadoSolicitudObjs;
    @BeforeEach
    void init(){
        ObjectMapper objectMapper = new ObjectMapper();
    }
    @Test
    public void crearObjetoActualizar() throws IOException, JSONException {
        String contenido = new String(Files.readAllBytes(Paths.get("src/test/resources/testJson.json")));
        JSONObject jsonObject = new JSONObject(contenido);
        estadoSolicitudObjs.crearObjetoActualizar(null,jsonObject,false,"");
    }
}