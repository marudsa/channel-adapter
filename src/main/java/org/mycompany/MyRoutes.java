package org.mycompany;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MyRoutes extends RouteBuilder {

    @Autowired
    private QueueConsumer queueConsumer;

    @Autowired
    private ResponseProcessor responseProcessor;

    @Override
    public void configure() throws Exception {
        // Configurar la ruta para recibir los mensajes de la cola
        from("jms:AMQ.CA.Q.RES")
                .process(queueConsumer)
                .to("direct:processResponse");

        // Configurar la ruta para procesar la respuesta
        from("direct:processResponse")
                .bean(responseProcessor, "process");
    }
}


