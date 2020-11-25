package edu.emmerson.camel3.cdi.rmq.route;


import java.time.Instant;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;


public class ProducerRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        StringBuilder sbPub = new StringBuilder();
        sbPub.append("rabbitmq:myexchange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&routingKey=main")
        .append("&queue=myqueue")
        .append("&durable=true")
        .append("&autoDelete=false")
        .append("&exclusive=false")
        .append("&exchangePattern=InOnly")
        ;
        
        from("direct:publishMessage")
        .routeId(ProducerRouteBuilder.class.getName())
        .process().message(m -> {
        	m.setHeader("custom.messageId", UUID.randomUUID().toString());
			m.setHeader("custom.currentTimeMillis", System.currentTimeMillis());
			m.setHeader("custom.Date", Instant.now().toString());
        })
        .log("Sending Message to RabbitMQ!")
        //.log("Message to be sent: ${body}")
        .to(sbPub.toString());
    }

}
