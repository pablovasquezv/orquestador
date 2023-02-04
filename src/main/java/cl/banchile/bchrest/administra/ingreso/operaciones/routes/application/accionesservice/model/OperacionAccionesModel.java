package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.accionesservice.model;

import banchile.cl.liquidacion_service._1.OrdenTO;
import cl.banchile.go.ejb.BolsaTO;
import cl.banchile.go.ejb.CanalAtencionTO;
import cl.banchile.go.ejb.IndicadorTO;
import cl.banchile.go.ejb.InstrumentoTO;
import cl.banchile.go.ejb.MercadoTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * @author Pablo
 *
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionAccionesModel extends AbstractOperacionModel {
    private BolsaTO bolsaTO;
    private CanalAtencionTO canal;
    private BigDecimal cantidadOrden;
    private BigDecimal comision;
    private String condicionDeLiquidacion;
    private Long correlativoCuenta;
    private String custodio;
    private String estado;
    private String fechaVigencia;
    private String indicadorOrdenRemate;
    private String indicadorPrecioLimite;
    private String indicadorPrimeraEmision;
    private IndicadorTO indicadorTOCustodia;
    private IndicadorTO indicadorTOPrecio;
    private IndicadorTO indicadorTOValidez;
    private InstrumentoTO instrumentoTO;
    private String medioCobroPago;
    private MercadoTO mercadoTO;
    private BigDecimal montoInversionOrden;
    private BigDecimal montoOperacion;
    private String nemoFondo;
    private String nombreSerie;
    private String numeroCuentaCorriente;
    private String numeroInformeRemate;
    private OrdenTO ordenLiquidacionTO;
    private BigDecimal precioLimite;
    private BigDecimal precioOrden;
    private String rutCliente;
    private Integer sruCliente;
    private String tipoOperacion;
}
