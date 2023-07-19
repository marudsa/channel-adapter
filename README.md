# Nombre del Proyecto: CHANNEL ADAPTER

## Descripción

Este proyecto es una aplicación Java encargada de procesar todos los mensajes que llegan al servicio expuesto en `http://localhost:3000/mock-endpoint`, el cual simula un servicio local del API Gateway. La aplicación recibe consultas en formato JSON y realiza diversas transformaciones para enviar mensajes en formato SOAP a una cola y procesar las respuestas recibidas.

## Requerimientos para Despliegue Local

Antes de desplegar el proyecto en modo local, asegúrate de cumplir con los siguientes requisitos:

1. ActiveMQ: Es necesario tener una instancia de ActiveMQ en ejecución. Se realizaron pruebas con la versión `apache-activemq-5.19.0-SNAPSHOT`. Asegúrate de que ActiveMQ esté configurado y funcionando correctamente.

2. IDE: Se recomienda utilizar RED HAT CodeReady Studio para trabajar con el proyecto, aunque también es posible usar Eclipse con los ajustes necesarios para Fuse.

3. SoapUI: Se utilizó SoapUI para lanzar peticiones al servicio REST simulado en `http://localhost:3000/mock-endpoint`. Asegúrate de tener SoapUI instalado para probar la funcionalidad de la aplicación.

## Arquitectura y Componentes

El proyecto está basado en Spring Boot y Apache Camel, y consta de varios componentes y clases importantes:

1. `camel-context.xml`: Este archivo define el contexto de Camel y contiene la configuración de enrutamiento. La ruta con ID "_route1" escucha en el endpoint HTTP utilizando Jetty en `http://localhost:3000/mock-endpoint`. Los mensajes recibidos en este endpoint se envían a un bean llamado "queueProducer" para su procesamiento, luego se registra un log del cuerpo del mensaje y finalmente se envían a otro bean llamado "responseProcessor" para su procesamiento adicional.

2. `ActiveMQConfig`: Esta clase es una configuración de Spring que define los beans necesarios para la conexión y el manejo de transacciones con ActiveMQ. Proporciona un bean "connectionFactory" que configura la conexión a ActiveMQ utilizando los valores definidos en el archivo `application-dev.properties`. También define un bean "jmsTransactionManager" para administrar las transacciones y un bean "messageConverter" para convertir mensajes.

3. `Application`: Esta clase es la aplicación principal de Spring Boot que inicia la aplicación. Está anotada con `@SpringBootApplication` y también importa el recurso XML de configuración `spring/camel-context.xml` utilizando la anotación `@ImportResource`. La aplicación se ejecuta mediante el método "main" que llama a `SpringApplication.run`.

4. `JsonToSoapTransformer`: Esta clase es un componente de Spring anotado con `@Component`. Proporciona un método "transformJsonToSoap" que transforma un mensaje en formato JSON a un mensaje en formato SOAP. Utiliza la biblioteca Jackson para convertir el mensaje JSON a un objeto Java y luego crea un documento XML utilizando el API DOM. El mensaje SOAP resultante se devuelve como una cadena de texto.

5. `MyMessageTransformer`: Esta clase es un componente de Spring anotado con `@Component`. Proporciona un método "transformSoapToJson" que transforma un mensaje en formato SOAP a un mensaje en formato JSON. Utiliza el API DOM para analizar el mensaje SOAP y extraer los valores necesarios. Luego, crea un objeto Java que representa el mensaje JSON y utiliza la biblioteca Jackson para convertirlo a una cadena de texto JSON.

6. `MyRoutes`: Esta clase es un componente de Spring anotado con `@Component` y extiende `RouteBuilder` de Camel. Define las rutas de Camel para procesar los mensajes recibidos. Dentro del método `configure`, se configura una ruta que consume mensajes de la cola `AMQ.CA.Q.RES` y los envía al bean "queueConsumer" para su procesamiento. Luego, la ruta "direct:processResponse" procesa la respuesta y llama al bean "responseProcessor" para su procesamiento adicional.

7. `QueueConsumer`: Esta clase es un componente de Spring anotado con `@Component` e implementa la interfaz `Processor` de Camel. Se utiliza en la ruta para consumir mensajes de la cola y realizar el procesamiento necesario. En el método `process`, se recibe un mensaje SOAP, se llama al bean "messageTransformer" para transformarlo a JSON y se establece el resultado como el cuerpo de salida del intercambio.

8. `QueueProducer`: Esta clase es un componente de Spring anotado con `@Component`. Proporciona un método "sendMessage" que recibe un mensaje en formato JSON, lo transforma a un mensaje SOAP utilizando el bean "jsonToSoapTransformer" y lo envía a la cola `AMQ_VALIDENTIDADREGISTRADURIA.ESB1.Q.IN` utilizando el bean "jmsTemplate" de Spring JMS.

9. `ResponseProcessor`: Esta clase es un componente de Spring anotado con `@Component`. Proporciona un método "process" que procesa la respuesta recibida en la ruta. En este caso, simplemente imprime la respuesta recibida.

## Flujo General del Proyecto

A continuación, se muestra el flujo general de la aplicación a través del archivo `camel-context.xml`:

![Flujo General](https://github.com/marudsa/channel-adapter/assets/110506792/253a018d-e7c0-4a82-a536-455b0492bbee)


## Instrucciones para Ejecutar

Sigue estos pasos para ejecutar el proyecto en modo local:

1. Asegúrate de tener ActiveMQ en ejecución y configurado correctamente con la versión `apache-activemq-5.19.0-SNAPSHOT`.

2. Importa el proyecto en tu IDE (preferiblemente RED HAT CodeReady Studio o Eclipse con los ajustes para Fuse).

3. Ejecuta la clase `Application` para iniciar la aplicación.

4. Utiliza SoapUI u otra herramienta para enviar peticiones al servicio REST simulado en `http://localhost:3000/mock-endpoint`.

5. Observa los registros en la consola y el procesamiento de mensajes a través de ActiveMQ y las rutas de Camel.

¡Listo! Ahora la aplicación está en funcionamiento y procesará los mensajes enviados al servicio REST simulado.

## Notas Adicionales

Si deseas realizar ajustes o configuraciones específicas, asegúrate de consultar la documentación del proyecto y revisar las clases y archivos mencionados anteriormente. También puedes agregar más información relevante al README.md para futuros colaboradores o usuarios del proyecto.
