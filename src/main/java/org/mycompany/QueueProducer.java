package org.mycompany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


@Component
public class QueueProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JsonToSoapTransformer jsonToSoapTransformer;

    public void sendMessage(String jsonMessage) {
        try {
            // Transformar el JSON a SOAP
            String soapMessage = jsonToSoapTransformer.transformJsonToSoap(jsonMessage);

            // Enviar el mensaje a la cola
            jmsTemplate.convertAndSend("AMQ_VALIDENTIDADREGISTRADURIA.ESB1.Q.IN", soapMessage);
        } catch (Exception e) {
            // Manejar la excepción aquí o lanzar una nueva excepción personalizada
            e.printStackTrace();
        }
    }
}

