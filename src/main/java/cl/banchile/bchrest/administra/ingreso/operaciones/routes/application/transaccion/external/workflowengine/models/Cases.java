package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Pablo
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"_case"})
public class Cases {
    @XmlElement(name = "Case", required = true)
    protected Case _case;

    public Case getCase() {
        return _case;
    }

    public void setCase(Case value) {
        this._case = value;
    }
}