package edu.emmerson.camel3.cdi.rmq;


import java.time.Instant;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;


public class ProducerRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure we want to use undertow as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("undertow")
            // use json binding mode so Camel automatic binds json <--> pojo
            //.bindingMode(RestBindingMode.json)
            // and output using pretty print
            //.dataFormatProperty("prettyPrint", "true")
            // setup context path on localhost and port number that netty will use
            .contextPath("/").host("0.0.0.0").port(8080)
            // add swagger api-doc out of the box
            .apiContextPath("/api-doc")
            	.apiContextRouteId("api-doc-endpoint")
                .apiProperty("api.title", "Producer API").apiProperty("api.version", "1.0.0")
                // and enable CORS
                .apiProperty("cors", "true");

        // this user REST service is json only
        rest("/rmq").id("rmq-endpoint").description("RabbitMQ rest service")
            .consumes("application/json").produces("application/json")

            .post("/publish").id("rmq-publish-resource").description("Publish a message in RabbitMQ")
                .responseMessage().code(204).message("Message storaged").endResponseMessage()
                .to("direct:publishMessage");
        
        StringBuilder sbPub = new StringBuilder();
        sbPub.append("rabbitmq:myexchange?")
        .append("connectionFactory=#producerConnectionFactoryService")
        .append("&queue=myqueue")
        .append("&durable=true")
        .append("&autoDelete=false")
        .append("&exclusive=false")
        .append("&exchangePattern=InOnly")
        .append("&deadLetterExchange=myexchangeDLQ")
        .append("&deadLetterExchangeType=direct")
        .append("&deadLetterQueue=myqueueDLQ")
        ;
        
        from("direct:publishMessage")
        .routeId(ProducerRouteBuilder.class.getName())
        .process().message(m -> {
        	m.setHeader("custom.messageId", UUID.randomUUID().toString());
			m.setHeader("custom.currentTimeMillis", System.currentTimeMillis());
			m.setHeader("custom.Date", Instant.now().toString());
        })
        //.log("Message to be sent: ${body}")
        //.log("Message to be sent: ${headers}")
        .to(sbPub.toString());
    }

}
