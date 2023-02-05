package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model.OperacionFondoMutuoModel;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.MonedaExtranjeraServiceHandler;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.BizagiObj;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.EjecutaBizagiWorkflowengine;
import cl.banchile.ingope.FirmarDocumentos;
import cl.banchile.ingope.Ingope;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.tempuri.CreateCasesResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Pablo
 *
 */


@SpringBootTest
@Slf4j
class FondosMutuosTest {
    private OperacionFondoMutuoModel requestOperacionTest,requestOperacion;
    private ObjectMapper objectMapper;
    private FondosMutuos fondosMutuos;
    @Autowired
    private EjecutaBizagiWorkflowengine ejecutaBizagiWorkflowengine;
    @Autowired
    private BizagiObj bizagiObj;
    @Autowired
    private MonedaExtranjeraServiceHandler service;
    @BeforeEach
    void init(){
        requestOperacionTest= new OperacionFondoMutuoModel();
        requestOperacion= new OperacionFondoMutuoModel();
        objectMapper= new ObjectMapper();
        fondosMutuos=new FondosMutuos();
    }
    //@Test
    void operacionFondosMutuosRNV() {
        try {
            requestOperacionTest.setCanalOperacion("WEB.BCH");
            Ingope objIngope = objectMapper.readValue(new File("src/test/resources/ingopernv.json"), Ingope.class);
            requestOperacion=fondosMutuos.operacionFondosMutuosRNV(objIngope,100L);
            Assert.isInstanceOf(OperacionFondoMutuoModel.class,requestOperacion);
            Assertions.assertEquals(requestOperacionTest.getCanalOperacion(),requestOperacion.getCanalOperacion());
            System.out.println("Objeto=>"+ requestOperacion.getCanalOperacion());
        } catch (IOException e) {
            System.out.println("Error"+ e.getMessage());
        }
    }
    @Test
    public void bizagiProceso() {
        CreateCasesResponse.CreateCasesResult result = ejecutaBizagiWorkflowengine.createCases(bizagiObj.crateCaseInfoTest());
        try {
            log.info("llamado exitosox =>{}", objectMapper.writeValueAsString(result.getContent()));
        } catch (Exception e) {
            e.getMessage();
        }
    }
}