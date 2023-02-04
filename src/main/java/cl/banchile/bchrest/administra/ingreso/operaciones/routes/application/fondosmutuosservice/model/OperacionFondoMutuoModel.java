package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.fondosmutuosservice.model;

import banchile.cl.liquidacion_service._1.OrdenTO;
import cl.banchile.go.ejb.CuentaFondoMutuoTO;
import cl.banchile.go.ejb.InstruccionCobroTO;
import cl.banchile.go.ejb.InstruccionPagoTO;
import cl.banchile.go.ejb.OperacionFondoMutuo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @author Pablo
 *
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionFondoMutuoModel extends AbstractOperacionModel {
    private String codigoOperacion;
    private String codigoOperacionContraparte;
    private CuentaFondoMutuoTO cuentaFondoMutuoTO;
    private Long idOrigenDeFondo;
    private Boolean indicador18Q;
    private Boolean indicadorCuentaNueva;
    private String indicadorCuotaPeso;
    private String indicadorRescateTotal;
    private List<OperacionFondoMutuo> listOperacionFondoMutuo;
    private List<InstruccionCobroTO> listaInstruccionCobroTO;
    private List<InstruccionPagoTO> listaInstruccionPagoTO;
    private OrdenTO ordenTO;
    private String tipoCurseRescate;
}