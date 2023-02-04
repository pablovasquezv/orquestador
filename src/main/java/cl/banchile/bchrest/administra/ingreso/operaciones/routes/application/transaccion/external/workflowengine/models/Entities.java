package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Pablo
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "pFirmarDocumentosv2"
})
public class Entities {
    @XmlElement(name = "PFirmarDocumentosv2", required = true)
    protected PFirmarDocumentosv2 pFirmarDocumentosv2;
    public PFirmarDocumentosv2 getPFirmarDocumentosv2() {
        return pFirmarDocumentosv2;
    }
    public void setPFirmarDocumentosv2(PFirmarDocumentosv2 value) {
        this.pFirmarDocumentosv2 = value;
    }
}
