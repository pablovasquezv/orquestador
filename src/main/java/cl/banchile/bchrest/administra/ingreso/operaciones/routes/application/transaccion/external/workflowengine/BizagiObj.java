package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.Case;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.Cases;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.BizAgiWSParam;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.CuentaCliente;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.Entities;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.GFirmantes;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.GFirmantess;
import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.models.PFirmarDocumentosv2;
import cl.banchile.ingope.FirmarDocumentos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tempuri.CreateCases;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pablo
 *
 */

@Slf4j
@Component
public class BizagiObj {
    private String xml;
    private CreateCases.CasesInfo casesInfo = new CreateCases.CasesInfo();
    private List<Object> content = new ArrayList();
    public CreateCases.CasesInfo crateCaseInfo(FirmarDocumentos firmarDocumentos, long idTransaccion, int numeroOperacion) {
        xml = "";
        casesInfo = new CreateCases.CasesInfo();
        content = new ArrayList();
        BizAgiWSParam bizAgiWSParam = new BizAgiWSParam();
        Cases cases = new Cases();
        Case kase = new Case();
        Entities entities = new Entities();
        CuentaCliente cuentaCliente = new CuentaCliente();
        GFirmantess gFirmantess = new GFirmantess();
        PFirmarDocumentosv2 firmDoc = new PFirmarDocumentosv2();
        kase.setProcess("FirmardocumentosV2");
        firmDoc.setIdentificadorContrato(firmarDocumentos.getIdentificadorContrato());
        cuentaCliente.setTipoCliente(firmarDocumentos.getCuentaCliente().getTipoCliente());
        cuentaCliente.setRut(firmarDocumentos.getCuentaCliente().getRut());
        cuentaCliente.setDigitoVerificador(firmarDocumentos.getCuentaCliente().getDigitoVerificador());
        cuentaCliente.setNombredeCuenta(firmarDocumentos.getCuentaCliente().getNombredeCuenta());
        cuentaCliente.setAccountId(firmarDocumentos.getCuentaCliente().getAccountId());
        cuentaCliente.setNumerodeCuenta(String.valueOf(firmarDocumentos.getCuentaCliente().getNumerodeCuenta()));
        cuentaCliente.setCuentaInversion(firmarDocumentos.getCuentaCliente().getCuentaInversion());
        cuentaCliente.setIDContacto(firmarDocumentos.getCuentaCliente().getIDContacto());
        firmDoc.setCuentaCliente(cuentaCliente);
        firmDoc.setIdTransaccion(String.valueOf(idTransaccion));
        firmDoc.setTiempoEnvioAlarma(firmarDocumentos.getTiempoEnvioAlarma());
        firmDoc.setTipoDocumento((short) 1);
        if(firmarDocumentos.getGFirmantess().getGFirmantes()!=null){
            firmarDocumentos.getGFirmantess().getGFirmantes().forEach(gFirmantes1 -> {
                GFirmantes gFirmantes = new GFirmantes();
                gFirmantes.setNombreFirmante(gFirmantes1.getNombreFirmante());
                gFirmantes.setRutFirmante(gFirmantes1.getRutFirmante());
                gFirmantes.setIdentificadorFirmante(gFirmantes1.getIdentificadorFirmante());
                gFirmantes.setCorreoElectronico(gFirmantes1.getCorreoElectronico());
                gFirmantess.setGFirmantes(gFirmantes);
            });
        }
        firmDoc.setGFirmantess(gFirmantess);
        firmDoc.setNombreCabecera("Firma Comprobante Ope.(" + numeroOperacion + ")");
        firmDoc.setIndicadorEnvioAlertaONE(false);
        entities.setPFirmarDocumentosv2(firmDoc);
        kase.setEntities(entities);
        cases.setCase(kase);
        bizAgiWSParam.setCases(cases);
        bizAgiWSParam.setDomain("domain");
        bizAgiWSParam.setUserName("admon");
        generarxml(bizAgiWSParam);
        node();
        return casesInfo;
    }
    public CreateCases.CasesInfo crateCaseInfoTest() {
        casesInfo = new CreateCases.CasesInfo();
        content = new ArrayList();
        xml = "<BizAgiWSParam>\n" +
                "               <domain>domain</domain>\n" +
                "               <userName>integrabizagi</userName>\n" +
                "               <Cases>\n" +
                "                  <Case>\n" +
                "                     <Process>FirmardocumentosV2</Process>\n" +
                "                     <Entities>\n" +
                "                        <PFirmarDocumentosv2>\n" +
                "                         <IdentificadorContrato>52</IdentificadorContrato>\n" +
                "                            <CuentaCliente>\n" +
                "                              <TipoCliente>2</TipoCliente>\n" +
                "                              <Rut>2634691</Rut>\n" +
                "                              <DigitoVerificador>6</DigitoVerificador>\n" +
                "                              <NombredeCuenta>Pedro Llorens Sabate</NombredeCuenta>\n" +
                "                              <AccountId>B3291A51-7416-E811-80D4-000C29AB8330</AccountId>\n" +
                "                            </CuentaCliente>\n" +
                "                           <IdTransaccion>81f336da-3101-492d-94eb-d310b124739a</IdTransaccion>\n" +
                "                           <TipoDocumento>0</TipoDocumento>\n" +
                "                           <GFirmantess>\n" +
                "                              <GFirmantes>\n" +
                "                                 <NombreFirmante>Pedro Llorens Sabate</NombreFirmante>\n" +
                "                                 <RutFirmante>2634691</RutFirmante>\n" +
                "                                 <IdentificadorFirmante>b3291a51-7416-e811-80d4-000c29ab8330</IdentificadorFirmante>\n" +
                "                                 <CorreoElectronico>magicocarella@gmail.com</CorreoElectronico>\n" +
                "                              </GFirmantes>\n" +
                "                           </GFirmantess>\n" +
                "                           <NombreCabecera>Firma Comprobante de Operación</NombreCabecera>\n" +
                "                           <IndicadorEnvioAlertaONE>false</IndicadorEnvioAlertaONE>\n" +
                "                         </PFirmarDocumentosv2>\n" +
                "                     </Entities>\n" +
                "                  </Case>\n" +
                "               </Cases>\n" +
                "            </BizAgiWSParam>";
        node();
        log.info("Enviando casesInfo {}", casesInfo);
        return casesInfo;
    }
    public void generarxml(BizAgiWSParam dato) {
        StringWriter writer = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BizAgiWSParam.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            //Datos formateados con salto de linea y sangria
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //Especifica el valor del atributo xsi: schemaLocation para colocar en la salida XML
            //jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
            //Codificacion de salida
            //jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
            //El nombre del esquemaXSD para el atributo xsi: noNamespaceSchemaLocation
            //jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "miesquema.xsd");
            jaxbMarshaller.marshal(dato, writer);
            xml = writer.toString();
            //xml = limpiar(xml);
            log.info("XML enviado a documentos => " + xml);
        } catch (JAXBException ex) {
            log.error("Error generando XML", ex);
        }
    }
    public void node() {
        //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //DocumentBuilder builder;
        try {
            //factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            //builder = factory.newDocumentBuilder();
            //Document doc = builder.parse(new InputSource(new StringReader(xml)));
            //---
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
            //--
            doc.getFirstChild().getNodeValue();
            doc.getDocumentElement().normalize();
            content.add(doc.getFirstChild());
            casesInfo.setContent(content);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error("error en BizagiObj Node()", e);
        }
    }
}