package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.convertidor;


import cl.banchile.bchrest.administra.ingreso.operaciones.common.utils.Utils;
import cl.banchile.ingope.Ingope;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Pablo
 *
 */

@Slf4j
public class XmlToIngope {

    @Autowired
    Utils utils;
    @Test
    public void convertir(){

        JAXBContext jaxbContext;

        ObjectMapper objectMapper = new ObjectMapper();

        {
            try {
                String xml = new String(Files.readAllBytes(Paths.get("src/test/resources/ingdiv.xml")));

                jaxbContext = JAXBContext.newInstance(Ingope.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                xml=limpiar(xml);
                StringReader reader = new StringReader(xml);

                Ingope ingreso = (Ingope) jaxbUnmarshaller.unmarshal(reader);
                String json = objectMapper.writeValueAsString(ingreso);
                log.info("JSON => {}",json);

            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}

    public static String limpiar(String xml) {

        xml=xml.replace("<ns2:createCases xmlns:ns2=\"http://tempuri.org/\">\n", "");
        xml=xml.replace("<ns2:casesInfo>\n", "");
        xml=xml.replace("</ns2:casesInfo>\n", "");
        xml=xml.replace("<BizAgiWSParam>\n", "");
        xml=xml.replace("<Cases>\n", "");
        xml=xml.replace("<Case>\n", "");
        xml=xml.replace("<Process>Ingresaroperacion</Process>","");
        xml=xml.replace("<Entities>\n", "");
        xml=xml.replace("</Entities>\n", "");
        xml=xml.replace("</Case>", "");
        xml=xml.replace("</Cases>", "");
        xml=xml.replace("</BizAgiWSParam>", "");
        xml=xml.replace("</ns2:createCases>", "");
        xml=xml.replace("<A", "<a");
        xml=xml.replace("<B", "<b");
        xml=xml.replace("<C", "<c");
        xml=xml.replace("<D", "<d");
        xml=xml.replace("<E", "<e");
        xml=xml.replace("<F", "<f");
        xml=xml.replace("<G", "<g");
        xml=xml.replace("<H", "<h");
        xml=xml.replace("<I", "<i");
        xml=xml.replace("<J", "<j");
        xml=xml.replace("<K", "<k");
        xml=xml.replace("<L", "<l");
        xml=xml.replace("<M", "<m");
        xml=xml.replace("<N", "<n");
        xml=xml.replace("<O", "<o");
        xml=xml.replace("<P", "<p");
        xml=xml.replace("<Q", "<q");
        xml=xml.replace("<R", "<r");
        xml=xml.replace("<S", "<s");
        xml=xml.replace("<T", "<t");
        xml=xml.replace("<U", "<u");
        xml=xml.replace("<V", "<v");
        xml=xml.replace("<W", "<w");
        xml=xml.replace("<X", "<x");
        xml=xml.replace("<Y", "<y");
        xml=xml.replace("<Z", "<z");
        xml=xml.replace("</A", "</a");
        xml=xml.replace("</B", "</b");
        xml=xml.replace("</C", "</c");
        xml=xml.replace("</D", "</d");
        xml=xml.replace("</E", "</e");
        xml=xml.replace("</F", "</f");
        xml=xml.replace("</G", "</g");
        xml=xml.replace("</H", "</h");
        xml=xml.replace("</I", "</i");
        xml=xml.replace("</J", "</j");
        xml=xml.replace("</K", "</k");
        xml=xml.replace("</L", "</l");
        xml=xml.replace("</M", "</m");
        xml=xml.replace("</N", "</n");
        xml=xml.replace("</O", "</o");
        xml=xml.replace("</P", "</p");
        xml=xml.replace("</Q", "</q");
        xml=xml.replace("</R", "</r");
        xml=xml.replace("</S", "</s");
        xml=xml.replace("</T", "</t");
        xml=xml.replace("</U", "</u");
        xml=xml.replace("</V", "</v");
        xml=xml.replace("</W", "</w");
        xml=xml.replace("</X", "</x");
        xml=xml.replace("</Y", "</y");
        xml=xml.replace("</Z", "</z");


        xml=xml.replace("<pIngresaroperacion>", "<Ingope>");
        xml=xml.replace("</pIngresaroperacion>", "</Ingope>");
        return xml;

    }


}