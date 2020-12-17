package edu.emmerson.camel3.cdi.rmq.controlbus;


import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;


public class MngtProducerRouteBuilder extends RouteBuilder {
	
	public static final String MNGT_QUEUE_NAME = "cdi-rabbit-consumer-mngt-producer-" + UUID.randomUUID();
	
	public static String getQueueEndpoint() {
		StringBuilder sbPub = new StringBuilder();
        //
        //precondition - create manually the topic in rabbitmq
        //
        sbPub.append("rabbitmq:myTopicExhange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&exchangeType=direct")
        .append("&routingKey=${exchangeProperty.routingKey}")
        .append("&durable=false")
        .append("&autoDelete=false")
        .append("&exchangePattern=InOnly")
        .append("&exclusive=true")
        .append("&queue=").append(MngtProducerRouteBuilder.MNGT_QUEUE_NAME)
        ;
        return sbPub.toString();
	}

	public static LinkedHashMap<String, String> buildMngtMessage(String consumerRouteID, boolean suspend, String rabbitRoutingKey, int restartDelayInMilis) {
		LinkedHashMap<String, String> body = new LinkedHashMap<String, String>();
		body.put("routeId", consumerRouteID);
		body.put("suspend", suspend ? "true" : "false");
		body.put("routingKey", rabbitRoutingKey);
		body.put("restartDelay", "" + restartDelayInMilis);
		return body;
	}
	
	public static void buildMngtMessage(Message dm , String consumerRouteID, boolean suspend, String rabbitRoutingKey, int restartDelayInMilis) {
		LinkedHashMap<String, String> body = buildMngtMessage(consumerRouteID, suspend, rabbitRoutingKey, restartDelayInMilis);
		dm.setBody(body);
	}

    @Override
    public void configure() throws Exception {

        String id = MngtConstants.MNGT_PRODUCER_DIRECT_ROUTE_ID;
        
		from(MngtConstants.MNGT_PRODUCER_DIRECT_ENDPOINT).autoStartup(false) //TODO: enable
        .routeId(id)
        .process().message(m -> {
        	m.setHeader("custom.messageId", UUID.randomUUID().toString());
			m.setHeader("custom.currentTimeMillis", System.currentTimeMillis());
			m.setHeader("custom.Date", Instant.now().toString());
			
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> body = (LinkedHashMap<String, String>) m.getBody();
			//m.getExchange().setProperty("routingKey", body.get("routingKey"));
			
			m.setHeader(org.apache.camel.component.rabbitmq.RabbitMQConstants.ROUTING_KEY, body.get("routingKey"));
        })
        .log("Camel management controlbus sending event to topic: ${body}")
        .marshal().json(JsonLibrary.Jackson)
        .toD(getQueueEndpoint())
        .log("Camel management controlbus sending event finished!");
    }
    
}
