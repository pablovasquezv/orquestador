package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasccionliquidacion.external.impl;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.Util;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.trasaccionliquidacion.external.OperacionTransaccionLiquidacion;
import cl.banchile.go.ejb.OperacionFondoMutuo;
import cl.banchile.liquidaciones.java.eapsrv.ejb.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pablo
 *
 */

@Slf4j
@SpringBootTest(classes = OperacionTransaccionLiquidacionService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")

class OperacionTransaccionLiquidacionServiceTest {

    private static InstruccionPagoInTO requestObjPagoIn;
    private static InstruccionCobroInTO requestObjCobroIn;
    private static InstruccionCobroOutTO requestObjCobroOut;
    private static InstruccionPagoOutTO requestObjPagoOut;

    @MockBean
    private static OperacionTransaccionLiquidacionService operacionTransaccionLiquidacionService;

    @MockBean
    private static TransaccionLiquidacionServiceEjb operaTransaccionLiquidacionServiceEjb;

    @BeforeAll
    private static void setUp() {

       /* log.info("RUNNIG setUp");
        try{

            requestObjPagoIn = Util.objetoPagoIn();
            requestObjCobroIn = Util.objetoCobroIn();
            responseObj2 = Util.jsonResponse();

            Mockito.lenient().
                    doReturn(responseObj).
                    when(operacionBancoEJB).addOperacionFondoMutuo(requestObj);

            Mockito.lenient().
                    doReturn(responseObj2).
                    when(operacionBancoEJBAdapter).agregarOperacionFondoMutuo(requestObj);

        }catch (Exception e){
            log.error("Error en setUp");
        }*/


    }
    @Test
    void agregarInstruccionCobro() {
    }

    @Test
    void agregarInstruccionPago() {
    }
}