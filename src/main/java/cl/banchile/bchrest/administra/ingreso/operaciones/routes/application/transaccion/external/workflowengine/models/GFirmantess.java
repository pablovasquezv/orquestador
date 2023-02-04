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
        "gFirmantes"
})

public  class GFirmantess {
    @XmlElement(name = "GFirmantes", required = true)
    protected GFirmantes gFirmantes;
    public GFirmantes getGFirmantes() {
        return gFirmantes;
    }
    public void setGFirmantes(GFirmantes value) {
        this.gFirmantes = value;
    }
}
