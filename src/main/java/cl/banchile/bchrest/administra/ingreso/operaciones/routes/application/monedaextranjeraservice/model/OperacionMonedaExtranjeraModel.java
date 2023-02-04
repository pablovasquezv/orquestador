package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model.AbstractOperacionModel;
import cl.banchile.go.ejb.MonedaTO;
import cl.banchile.go.ejb.OrdenLiquidacionTO;
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
public class OperacionMonedaExtranjeraModel extends AbstractOperacionModel {
    private String canal;
    private String codigoOficina;
    private BigDecimal factorCalculoPreciosPuntas;
    private BigDecimal factorFijoParidad;
    private String indicadorCanalDeIngreso;
    private MonedaTO monedaCobroTO;
    private MonedaTO monedaPagoTO;
    private BigDecimal montoComisionCalculada;
    private BigDecimal montoComisionRealCalculada;
    private BigDecimal montoComisionRealSinIVA;
    private BigDecimal montoTotalPago;
    private String oficinaLiquidacion;
    private OrdenLiquidacionTO ordenLiquidacionTO;
    private BigDecimal paridad;
    private BigDecimal precioCompra;
    private BigDecimal precioMedio;
    private BigDecimal precioPunta;
    private BigDecimal precioVenta;
    private BigDecimal spreadAsociado;
}