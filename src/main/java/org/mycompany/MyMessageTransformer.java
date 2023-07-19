package org.mycompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import org.xml.sax.InputSource;


@Component
public class MyMessageTransformer {

	public String transformSoapToJson(String soapMessage) throws Exception {
	    try {
	        // Crear un documento XML a partir del mensaje SOAP
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse(new InputSource(new StringReader(soapMessage)));

	        // Obtener el elemento raíz del mensaje SOAP
	        Element root = document.getDocumentElement();
	        System.out.println("Root element: " + root.getNodeName());

	        // Buscar el elemento "consultarCedulasResponse"
	        Element consultarCedulasResponseElement = (Element) root.getElementsByTagName("exam:consultarCedulasResponse").item(0);
	        if (consultarCedulasResponseElement != null) {
	            // Obtener los elementos hijos del elemento "return"
	            Element returnElement = (Element) consultarCedulasResponseElement.getElementsByTagName("return").item(0);
	            if (returnElement != null) {
	                // Obtener el valor de los elementos dentro de "estadoConsulta"
	                Element estadoConsultaElement = (Element) returnElement.getElementsByTagName("estadoConsulta").item(0);
	                String numeroControl = estadoConsultaElement.getElementsByTagName("numeroControl").item(0).getTextContent();
	                String codError = estadoConsultaElement.getElementsByTagName("codError").item(0).getTextContent();
	                String descripcionError = estadoConsultaElement.getElementsByTagName("descripcionError").item(0).getTextContent();
	                String fechaHoraConsulta = estadoConsultaElement.getElementsByTagName("fechaHoraConsulta").item(0).getTextContent();

	                // Obtener los valores de los elementos dentro de "datosCedulas"
	                Element datosCedulasElement = (Element) returnElement.getElementsByTagName("datosCedulas").item(0);
	                Element datosElement = (Element) datosCedulasElement.getElementsByTagName("datos").item(0);
	                String nuip = datosElement.getElementsByTagName("nuip").item(0).getTextContent();
	                String codErrorDatos = datosElement.getElementsByTagName("codError").item(0).getTextContent();
	                String primerApellido = datosElement.getElementsByTagName("primerApellido").item(0).getTextContent();
	                String segundoApellido = datosElement.getElementsByTagName("segundoApellido").item(0).getTextContent();
	                String primerNombre = datosElement.getElementsByTagName("primerNombre").item(0).getTextContent();
	                String estadoCedula = datosElement.getElementsByTagName("estadoCedula").item(0).getTextContent();

	                // Crear un objeto JSON con los valores obtenidos
	                ObjectMapper mapper = new ObjectMapper();
	                MyMessageJson messageJson = new MyMessageJson();
	                messageJson.setNumeroControl(numeroControl);
	                messageJson.setCodError(codError);
	                messageJson.setDescripcionError(descripcionError);
	                messageJson.setFechaHoraConsulta(fechaHoraConsulta);
	                messageJson.setNuip(nuip);
	                messageJson.setCodErrorDatos(codErrorDatos);
	                messageJson.setPrimerApellido(primerApellido);
	                messageJson.setSegundoApellido(segundoApellido);
	                messageJson.setPrimerNombre(primerNombre);
	                messageJson.setEstadoCedula(estadoCedula);

	                String jsonMessage = mapper.writeValueAsString(messageJson);
	                System.out.println("Elemento Json: " + jsonMessage);
	                return jsonMessage;
	            }
	        }
	        throw new Exception("Element 'consultarCedulasResponse' or 'return' not found in the SOAP message.");
	    } catch (ParserConfigurationException | org.xml.sax.SAXException | IOException e) {
	        throw new Exception("Error transforming SOAP to JSON: " + e.getMessage(), e);
	    }
	}


	public static class MyMessageJson {
	    private String numeroControl;
	    private String codError;
	    private String descripcionError;
	    private String fechaHoraConsulta;
	    private String nuip;
	    private String codErrorDatos;
	    private String primerApellido;
	    private String segundoApellido;
	    private String primerNombre;
	    private String estadoCedula;

	    public String getNumeroControl() {
	        return numeroControl;
	    }

	    public void setNumeroControl(String numeroControl) {
	        this.numeroControl = numeroControl;
	    }

	    public String getCodError() {
	        return codError;
	    }

	    public void setCodError(String codError) {
	        this.codError = codError;
	    }

	    public String getDescripcionError() {
	        return descripcionError;
	    }

	    public void setDescripcionError(String descripcionError) {
	        this.descripcionError = descripcionError;
	    }

	    public String getFechaHoraConsulta() {
	        return fechaHoraConsulta;
	    }

	    public void setFechaHoraConsulta(String fechaHoraConsulta) {
	        this.fechaHoraConsulta = fechaHoraConsulta;
	    }

	    public String getNuip() {
	        return nuip;
	    }

	    public void setNuip(String nuip) {
	        this.nuip = nuip;
	    }

	    public String getCodErrorDatos() {
	        return codErrorDatos;
	    }

	    public void setCodErrorDatos(String codErrorDatos) {
	        this.codErrorDatos = codErrorDatos;
	    }

	    public String getPrimerApellido() {
	        return primerApellido;
	    }

	    public void setPrimerApellido(String primerApellido) {
	        this.primerApellido = primerApellido;
	    }

	    public String getSegundoApellido() {
	        return segundoApellido;
	    }

	    public void setSegundoApellido(String segundoApellido) {
	        this.segundoApellido = segundoApellido;
	    }

	    public String getPrimerNombre() {
	        return primerNombre;
	    }

	    public void setPrimerNombre(String primerNombre) {
	        this.primerNombre = primerNombre;
	    }

	    public String getEstadoCedula() {
	        return estadoCedula;
	    }

	    public void setEstadoCedula(String estadoCedula) {
	        this.estadoCedula = estadoCedula;
	    }
	}

}
