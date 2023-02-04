package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * @author Pablo
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "tipoCliente",
        "rut",
        "digitoVerificador",
        "nombredeCuenta",
        "accountId",
        "idContacto",
        "numerodeCuenta",
        "cuentaInversion"})

public class CuentaCliente {
    @XmlElement(name = "TipoCliente")
    @XmlSchemaType(name = "unsignedByte")
    protected short tipoCliente;
    @XmlElement(name = "Rut")
    @XmlSchemaType(name = "unsignedInt")
    protected long rut;
    @XmlElement(name = "DigitoVerificador")
    @XmlSchemaType(name = "unsignedByte")
    protected short digitoVerificador;
    @XmlElement(name = "NombredeCuenta", required = true)
    protected String nombredeCuenta;
    @XmlElement(name = "AccountId", required = true)
    protected String accountId;
    @XmlElement(name = "IDContacto", required = true)
    protected String idContacto;
    @XmlElement(name = "NumerodeCuenta")
    protected String numerodeCuenta;
    @XmlElement(name = "CuentaInversion")
    protected short cuentaInversion;

    public short getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(short value) {
        this.tipoCliente = value;
    }

    public long getRut() {
        return rut;
    }

    public void setRut(long value) {
        this.rut = value;
    }

    public short getDigitoVerificador() {
        return digitoVerificador;
    }

    public void setDigitoVerificador(short value) {
        this.digitoVerificador = value;
    }

    public String getNombredeCuenta() {
        return nombredeCuenta;
    }

    public void setNombredeCuenta(String value) {
        this.nombredeCuenta = value;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String value) {
        this.accountId = value;
    }

    public String getIDContacto() {
        return idContacto;
    }

    public void setIDContacto(String value) {
        this.idContacto = value;
    }

    public String getNumerodeCuenta() {
        return numerodeCuenta;
    }

    public void setNumerodeCuenta(String numerodeCuenta) {
        this.numerodeCuenta = numerodeCuenta;
    }

    public short getCuentaInversion() {
        return cuentaInversion;
    }

    public void setCuentaInversion(short cuentaInversion) {
        this.cuentaInversion = cuentaInversion;
    }
}