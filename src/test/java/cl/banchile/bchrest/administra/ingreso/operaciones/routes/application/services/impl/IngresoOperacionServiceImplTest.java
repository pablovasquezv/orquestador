package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.services.impl;

import cl.banchile.ingope.Ingope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pablo
 *
 */


class IngresoOperacionServiceImplTest {
    private IngresoOperacionServiceImpl ingresoOperacionService;
    private ResponseEntity response,responseTest;
    private ObjectMapper objectMapper;
    @BeforeEach
    void init(){
        ingresoOperacionService= new IngresoOperacionServiceImpl();
        objectMapper = new ObjectMapper();
    }
    @Test
    void prepararProducto() {
        try {
            Ingope objIngope = objectMapper.readValue(new File("src/test/resources/ingopernv.json"), Ingope.class);
            String result=ingresoOperacionService.prepararProducto(objIngope);
            Assertions.assertEquals(result,"FFMM");
            objIngope = objectMapper.readValue(new File("src/test/resources/ingopeACC.json"), Ingope.class);
            result= ingresoOperacionService.prepararProducto(objIngope);
            Assertions.assertEquals(result, "ACC");
            objIngope = objectMapper.readValue(new File("src/test/resources/ingopeMX.json"), Ingope.class);
            result= ingresoOperacionService.prepararProducto(objIngope);
            Assertions.assertEquals(result, "MEX");
            objIngope = objectMapper.readValue(new File("src/test/resources/ingopeACC2.json"), Ingope.class);
            result= ingresoOperacionService.prepararProducto(objIngope);
            Assertions.assertEquals(result, "NO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}