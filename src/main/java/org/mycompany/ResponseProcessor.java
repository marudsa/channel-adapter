package org.mycompany;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class ResponseProcessor {
    public void process(Exchange exchange) {
        String response = exchange.getIn().getBody(String.class);
        // Realizar el procesamiento de la respuesta aquí
        System.out.println("Respuesta recibida: " + response);
    }
}
