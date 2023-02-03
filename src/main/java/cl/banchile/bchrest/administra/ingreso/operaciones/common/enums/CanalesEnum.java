/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.enums;

/**
 * @author Pablo
 *
 */

public enum CanalesEnum {
	WEBBCO(1), 
	MOVILFIN(2), 
	MOVILAPV(3), 
	WEBEMP(4), 
	PTVBCO(5), 
	WEBBCH(6), 
	PTVBCH(7), 
	BANPAY(8), 
	AUTBCO(9), 
	APVSIF(10),
	MOVMIN(11), 
	MOVMIN2(12), 
	WEBBCH2(13);

	public final int idCanal;

	CanalesEnum(int idCanal) {
		this.idCanal = idCanal;
	}
}