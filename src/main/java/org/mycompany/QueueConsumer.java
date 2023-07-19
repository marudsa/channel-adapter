package org.mycompany;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class QueueConsumer implements Processor {

    @Autowired
    private MyMessageTransformer messageTransformer;

    @Override
    public void process(Exchange exchange) throws Exception {
        String soapMessage = exchange.getIn().getBody(String.class);
        String jsonMessage = messageTransformer.transformSoapToJson(soapMessage);

        // Procesar el mensaje JSON (puedes agregar tu lógica aquí)

        // Establecer el resultado transformado como el cuerpo de salida
        exchange.getOut().setBody(jsonMessage);
    }
}
