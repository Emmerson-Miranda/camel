package edu.emmerson.camel3.cdi.rmq;


import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;


public class MngtProducerRouteBuilder extends RouteBuilder {
	
	public static final String DIRECT_PUBLISH_MESSAGE_MNGT_PRODUCER_ENDPOINT = "direct:publishMessageMngtProducer";
    
	public static final String MNGT_QUEUE_NAME = "cdi-rabbit-consumer-mngt-producer-" ;//+ UUID.randomUUID();
	
	public static String getQueueEndpoint() {
		StringBuilder sbPub = new StringBuilder();
        //
        //precondition - create manually the topic in rabbitmq
        //
        sbPub.append("rabbitmq:myTopicExhange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&exchangeType=topic")
        .append("&routingKey=${exchangeProperty.routingKey}")
        .append("&durable=false")
        .append("&autoDelete=false")
        .append("&exchangePattern=InOnly")
        //.append("&exclusive=true")
        .append("&queue=").append(MngtProducerRouteBuilder.MNGT_QUEUE_NAME)
        ;
        return sbPub.toString();
	}
	
	public static void buildMngtMessage(Message dm , String consumerRouteID, boolean suspend, String rabbitRoutingKey) {
		LinkedHashMap<String, String> body = new LinkedHashMap<String, String>();
		body.put("routeId", consumerRouteID);
		body.put("suspend", suspend ? "true" : "false");
		body.put("routingKey", rabbitRoutingKey);
		dm.setBody(body);
	}

    @Override
    public void configure() throws Exception {

    	MetricsRoutePolicy mrp = new MetricsRoutePolicy();
		mrp.setNamePattern(ConsumerConstants.MNGT_PRODUCER_ROUTE_ID);
		mrp.setUseJmx(true);
		mrp.setJmxDomain(ConsumerConstants.JMX_DOMAIN_NAME);
		
        from(MngtProducerRouteBuilder.DIRECT_PUBLISH_MESSAGE_MNGT_PRODUCER_ENDPOINT)
        .routeId(ConsumerConstants.MNGT_PRODUCER_ROUTE_ID)
        .routePolicy(mrp)
        .process().message(m -> {
        	m.setHeader("custom.messageId", UUID.randomUUID().toString());
			m.setHeader("custom.currentTimeMillis", System.currentTimeMillis());
			m.setHeader("custom.Date", Instant.now().toString());
			
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> body = (LinkedHashMap<String, String>) m.getBody();
			//m.getExchange().setProperty("routingKey", body.get("routingKey"));
			
			m.setHeader(org.apache.camel.component.rabbitmq.RabbitMQConstants.ROUTING_KEY, body.get("routingKey"));
        })
        .log("Message to be sent: ${body}")
        .toD(getQueueEndpoint());
    }
    
}
