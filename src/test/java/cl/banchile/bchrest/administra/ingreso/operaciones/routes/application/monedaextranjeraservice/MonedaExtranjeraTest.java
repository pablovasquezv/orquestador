package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model.OperacionMonedaExtranjeraModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.InstruccionCobroObjs;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.InstruccionPagoObjs;
import cl.banchile.ingope.Ingope;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionCobroInTO;
import cl.banchile.liquidaciones.java.eapsrv.ejb.InstruccionPagoInTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Pablo
 *
 */

@SpringBootTest
class MonedaExtranjeraTest {
    private OperacionMonedaExtranjeraModel requestOperacionTest,requestOperacion;
    private InstruccionCobroInTO instruccionCobroInTO;
    private InstruccionPagoInTO instruccionPagoInTO;
    private ObjectMapper objectMapper;
    @Autowired
    private MonedaExtranjeraObjs monedaExtranjeraObjs;
    @Autowired
    private InstruccionPagoObjs instruccionPagoObjs;
    @BeforeEach
    void init(){
        requestOperacionTest= new OperacionMonedaExtranjeraModel();
        requestOperacion= new OperacionMonedaExtranjeraModel();
        objectMapper= new ObjectMapper();
    }
    @Test
    void operacionMonedaExtranjera() {
        try {
            requestOperacionTest.setCanalOperacion("WEB.BCH");
            Ingope objIngope = objectMapper.readValue(new File("src/test/resources/ingopeMX.json"), Ingope.class);
            requestOperacion = monedaExtranjeraObjs.crearObjetoMonedaExtranjera(objIngope);
            Assert.isInstanceOf(OperacionMonedaExtranjeraModel.class, requestOperacion);
            Assertions.assertEquals(requestOperacionTest.getCanalOperacion(), requestOperacion.getCanalOperacion());
            System.out.println("Objeto=> "+ requestOperacion.getCanalOperacion());
        } catch (IOException e) {
            System.out.println("Error"+ e.getMessage());
        }
    }
    @Test
    void crearInstruccionCobro(){
        try {
            Ingope objIngope = objectMapper.readValue(new File("src/test/resources/ingopeMX.json"), Ingope.class);
            JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/ingopeResultadoMX.json"))));
            /*Mockito.when(XML.toJSONObject(""))
                    .thenReturn(responseObj);*/
            objIngope.getPIngresarInstruccions().getPIngresarInstruccion().forEach(pIngresarInstruccion ->            {
                if (pIngresarInstruccion.getTipoInstruccion().equals("Cobro")) {
                    try {
                        instruccionPagoInTO = instruccionPagoObjs.crearInstruccionMonedaExtranjera(pIngresarInstruccion,objIngope, jsonObject);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
            Assert.notNull(instruccionPagoInTO, "Es nullo");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    void crearInstruccionPago() {
        try {
            Ingope objIngope = objectMapper.readValue(new File("src/test/resources/ingopeMX.json"), Ingope.class);
            JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/ingopeResultadoMX.json"))));
            objIngope.getPIngresarInstruccions().getPIngresarInstruccion().forEach(pIngresarInstruccion ->            {
                if (pIngresarInstruccion.getTipoInstruccion().equals("Pago")) {
                    //instruccionCobroInTO = InstruccionCobroObjs.crearInstruccionMonedaExtranjera(pIngresarInstruccion,objIngope, jsonObject,null);
                }
            });
            Assert.notNull(instruccionCobroInTO, "Es nullo");
            System.out.println(instruccionCobroInTO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}