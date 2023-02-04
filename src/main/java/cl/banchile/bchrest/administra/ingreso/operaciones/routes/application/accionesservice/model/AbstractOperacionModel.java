package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice.model;

import cl.banchile.go.ejb.CanalAtencionTO;
import cl.banchile.go.ejb.ClienteTO;
import cl.banchile.go.ejb.InstruccionCobroTO;
import cl.banchile.go.ejb.InstruccionPagoTO;
import cl.banchile.go.ejb.FechasOperacionTO;
import cl.banchile.go.ejb.InstruccionTO;
import cl.banchile.go.ejb.MensajeTO;
import cl.banchile.go.ejb.OperacionEnum;
import cl.banchile.go.ejb.ProductoEnum;
import lombok.Data;
import service.global.utilitario.banchile.cl._1.EmpleadoTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Pablo
 *
 */

@Data
public class AbstractOperacionModel {
    private CanalAtencionTO canalAtencionTO;
    private String canalOperacion;
    private ClienteTO clienteTO;
    private Long correlativo;
    private EmpleadoTO ejecutivoConectadoTO;
    private EmpleadoTO ejecutivoOperacionTO;
    private FechasOperacionTO fechaOperacionTO;
    private String idTransaccion;
    private Boolean indicadorReinversion;
    private List<InstruccionCobroTO> listInstruccionCobroTO;
    private List<InstruccionPagoTO> listInstruccionPagoTO;
    private List<InstruccionTO> listaInstruccionTO;
    private MensajeTO mensajeTO;
    private BigDecimal montoValorizado;
    private Long numeroOrden;
    private String observacion;
    private OperacionEnum tipoOperacionEnum;
    private ProductoEnum tipoProductoEnum;
    private BigDecimal unidadesTransadas;
    public AbstractOperacionModel() {
    }
}