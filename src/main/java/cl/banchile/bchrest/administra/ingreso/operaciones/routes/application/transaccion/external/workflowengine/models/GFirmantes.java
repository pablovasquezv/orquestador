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
        "nombreFirmante",
        "rutFirmante",
        "identificadorFirmante",
        "correoElectronico"
})
public  class GFirmantes {
    @XmlElement(name = "NombreFirmante", required = true)
    protected String nombreFirmante;
    @XmlElement(name = "RutFirmante")
    @XmlSchemaType(name = "unsignedInt")
    protected long rutFirmante;
    @XmlElement(name = "IdentificadorFirmante", required = true)
    protected String identificadorFirmante;
    @XmlElement(name = "CorreoElectronico", required = true)
    protected String correoElectronico;
    public String getNombreFirmante() {
        return nombreFirmante;
    }
    public void setNombreFirmante(String value) {
        this.nombreFirmante = value;
    }
    public long getRutFirmante() {
        return rutFirmante;
    }
    public void setRutFirmante(long value) {
        this.rutFirmante = value;
    }
    public String getIdentificadorFirmante() {
        return identificadorFirmante;
    }
    public void setIdentificadorFirmante(String value) {
        this.identificadorFirmante = value;
    }
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    public void setCorreoElectronico(String value) {
        this.correoElectronico = value;
    }
}