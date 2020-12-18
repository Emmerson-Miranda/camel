package edu.emmerson.camel3.cdi.rmq.route;


import java.time.Instant;
import java.util.UUID;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.model.dataformat.JsonLibrary;

import edu.emmerson.camel3.cdi.rmq.util.ConsumerConstants;
import edu.emmerson.camel3.cdi.rmq.util.MetricsFactory;


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
        
        MetricsRoutePolicy mrp = MetricsFactory.createMetricsRoutePolicy(ConsumerConstants.PRODUCER_DIRECT_ROUTE_ID);
    	
        from("direct:publishMessage")
        .routeId(ConsumerConstants.PRODUCER_DIRECT_ROUTE_ID)
        .routePolicy(mrp)
        .process(e -> {
        	Message m  = e.getMessage();
        	m.setHeader("custom.messageId", UUID.randomUUID().toString());
			m.setHeader("custom.currentTimeMillis", System.currentTimeMillis());
			m.setHeader("custom.Date", Instant.now().toString());
			m.setHeader("custom.correlationID", m.getHeader("X-Correlation-ID"));
			m.setHeader("custom.x-sleep", m.getHeader("x-sleep"));
        })
        .log("Sending Message to RabbitMQ!")
        .marshal().json(JsonLibrary.Jackson) //without this a conversion error will happen - No type converter available to convert from type: java.util.LinkedHashMap to the required type: byte[]
        .to(sbPub.toString());
    }

}
