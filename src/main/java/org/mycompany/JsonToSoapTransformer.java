package org.mycompany;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Component
public class JsonToSoapTransformer {

    public String transformJsonToSoap(String jsonMessage) throws Exception {
        try {
            // Crear un objeto JSON a partir del mensaje JSON
            ObjectMapper mapper = new ObjectMapper();
            MyMessageJson messageJson = mapper.readValue(jsonMessage, MyMessageJson.class);

            // Crear un documento XML para el mensaje SOAP
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Crear el elemento raíz del mensaje SOAP
            Element envelope = document.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "soapenv:Envelope");
            envelope.setAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            envelope.setAttribute("xmlns:exam", "http://example.com");

            Element header = document.createElement("soapenv:Header");
            envelope.appendChild(header);

            Element body = document.createElement("soapenv:Body");
            envelope.appendChild(body);

            // Crear el elemento "consultarCedulasRequest" con los valores del JSON
            Element consultarCedulasRequest = document.createElement("exam:consultarCedulasRequest");
            body.appendChild(consultarCedulasRequest);

            Element tipoDocumento = document.createElement("tipoDocumento");
            tipoDocumento.setTextContent(messageJson.getTipoDoc());
            consultarCedulasRequest.appendChild(tipoDocumento);

            Element documento = document.createElement("documento");
            documento.setTextContent(messageJson.getDocumento());
            consultarCedulasRequest.appendChild(documento);

            document.appendChild(envelope);

            // Convertir el documento XML a cadena de texto
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no"); // Establecer standalone="no"
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            String soapMessage = writer.toString();

            return soapMessage;
        } catch (Exception e) {
            throw new Exception("Error transforming JSON to SOAP: " + e.getMessage(), e);
        }
    }

    public static class MyMessageJson {
        private String tipoDoc;
        private String documento;

        public String getTipoDoc() {
            return tipoDoc;
        }

        public void setTipoDoc(String tipoDoc) {
            this.tipoDoc = tipoDoc;
        }

        public String getDocumento() {
            return documento;
        }

        public void setDocumento(String documento) {
            this.documento = documento;
        }
    }
}
