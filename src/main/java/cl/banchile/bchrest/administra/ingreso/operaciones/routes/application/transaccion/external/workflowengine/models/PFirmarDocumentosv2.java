package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * @author Pablo
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "tipoDocumento",
        "identificadorContrato",
        "idTransaccion",
        "cuentaCliente",
        "xmlData",
        "gFirmantess",
        "nombreCabecera",
        "indicadorEnvioAlertaONE",
        "tiempoEnvioAlarma"
})

public  class PFirmarDocumentosv2 {
    @XmlElement(name = "IdentificadorContrato")
    @XmlSchemaType(name = "unsignedByte")
    protected short identificadorContrato;
    @XmlElement(name = "TipoDocumento")
    @XmlSchemaType(name = "unsignedByte")
    protected short tipoDocumento;
    @XmlElement(name = "CuentaCliente", required = true)
    protected CuentaCliente cuentaCliente;
    @XmlElement(name = "XMLData", required = true)
    protected String xmlData;
    @XmlElement(name = "GFirmantess", required = true)
    protected GFirmantess gFirmantess;
    @XmlElement(name = "NombreCabecera", required = true)
    protected String nombreCabecera;
    @XmlElement(name = "IndicadorEnvioAlertaONE")
    protected boolean indicadorEnvioAlertaONE;
    @XmlElement(name = "IdTransaccion")
    protected String idTransaccion;
    @XmlElement(name = "TiempoEnvioAlarma")
    protected short tiempoEnvioAlarma;

    public short getIdentificadorContrato() {
        return identificadorContrato;
    }
    public void setIdentificadorContrato(short value) {
        this.identificadorContrato = value;
    }
    public CuentaCliente getCuentaCliente() {
        return cuentaCliente;
    }
    public void setCuentaCliente(CuentaCliente value) {
        this.cuentaCliente = value;
    }
    public String getXMLData() {
        return xmlData;
    }
    public void setXMLData(String value) {
        this.xmlData = value;
    }
    public GFirmantess getGFirmantess() {
        return gFirmantess;
    }
    public void setGFirmantess(GFirmantess value) {
        this.gFirmantess = value;
    }
    public String getNombreCabecera() {
        return nombreCabecera;
    }
    public void setNombreCabecera(String value) {
        this.nombreCabecera = value;
    }
    public boolean isIndicadorEnvioAlertaONE() {
        return indicadorEnvioAlertaONE;
    }
    public void setIndicadorEnvioAlertaONE(boolean value) {
        this.indicadorEnvioAlertaONE = value;
    }
    public String getIdTransaccion() { return idTransaccion;}
    public void setIdTransaccion(String idTransaccion) { this.idTransaccion = idTransaccion;}
    public short getTipoDocumento() { return tipoDocumento;}
    public void setTipoDocumento(short tipoDocumento) { this.tipoDocumento = tipoDocumento;}

    public short getTiempoEnvioAlarma() {
        return tiempoEnvioAlarma;
    }

    public void setTiempoEnvioAlarma(short tiempoEnvioAlarma) {
        this.tiempoEnvioAlarma = tiempoEnvioAlarma;
    }
}