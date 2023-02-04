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
        "process",
        "entities"
})
public  class Case {
    @XmlElement(name = "Process", required = true)
    protected String process;
    @XmlElement(name = "Entities", required = true)
    protected Entities entities;
    public String getProcess() {
        return process;
    }
    public void setProcess(String value) {
        this.process = value;
    }
    public Entities getEntities() {
        return entities;
    }
    public void setEntities(Entities value) {
        this.entities = value;
    }

}