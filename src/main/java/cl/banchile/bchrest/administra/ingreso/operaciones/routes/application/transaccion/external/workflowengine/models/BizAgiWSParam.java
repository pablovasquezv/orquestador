package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.Cases;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Pablo
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "domain",
        "userName",
        "cases"
})

@XmlRootElement(name = "BizAgiWSParam")
public class BizAgiWSParam {
    @XmlElement(required = true)
    protected String domain;
    @XmlElement(required = true)
    protected String userName;
    @XmlElement(name = "Cases", required = true)
    protected Cases cases;
    public String getDomain() {
        return domain;
    }
    public void setDomain(String value) {
        this.domain = value;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String value) {
        this.userName = value;
    }
    public Cases getCases() {
        return cases;
    }
    public void setCases(Cases value) {
        this.cases = value;
    }
}