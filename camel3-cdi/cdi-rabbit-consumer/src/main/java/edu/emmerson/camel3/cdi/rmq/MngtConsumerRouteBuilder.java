package edu.emmerson.camel3.cdi.rmq;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/components/latest/controlbus-component.html
 *
 */
public class MngtConsumerRouteBuilder extends RouteBuilder {
	
	public static final String MNGT_QUEUE_NAME = "cdi-rabbit-consumer-mngt-consumer-" + UUID.randomUUID();

	public static final String DIRECT_TARGET_MNGT_ENDPOINT = "direct:targetMngt";
	
	public static String getQueueEndpoint() {
        StringBuilder sbConsumer = new StringBuilder();
        sbConsumer.append("rabbitmq:myTopicExhange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&exchangeType=topic")
        .append("&routingKey=").append(ConsumerRouteBuilder.RABBITMQ_ROUTING_KEY)
        .append("&durable=false")
        .append("&autoDelete=false")
        .append("&exchangePattern=InOut")
        .append("&queue=").append(MngtConsumerRouteBuilder.MNGT_QUEUE_NAME)
        
        .append("&exclusive=true")
        .append("&autoAck=false");
        return sbConsumer.toString();
	}
	
    @Override
    public void configure() throws Exception {
    	
        //
        //consuming management messages
        //
        from(getQueueEndpoint())
	        .routeId(MngtConsumerRouteBuilder.class.getName() + "-Consumer")
		    .to(DIRECT_TARGET_MNGT_ENDPOINT);
	    
        MetricsRoutePolicy mrp = new MetricsRoutePolicy();
		mrp.setNamePattern(ConsumerConstants.MNGT_CONSUMER_ROUTE_ID);
		mrp.setUseJmx(true);
		mrp.setJmxDomain(ConsumerConstants.JMX_DOMAIN_NAME);	
		
        //
        //processing message
        //	
        from(DIRECT_TARGET_MNGT_ENDPOINT)
	        .routeId(ConsumerConstants.MNGT_CONSUMER_ROUTE_ID)
	        .routePolicy(mrp)
	        .log(">>> ------------------------------------------------------------------")
	        .log("Mngt Message body: ${body}")
	        .process().message(m -> {
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> body = (LinkedHashMap<String, String>) m.getBody();
				m.getExchange().setProperty("route-id", body.get("routeId"));
				m.getExchange().setProperty("route-action", "true".equals(body.get("suspend")) ? "suspend" : "resume");
	        })
	        //.toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=${exchangeProperty.route-action}")
	        .toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=restart&restartDelay=10000")
	        .toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=status")
	        .toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=stats")
	        .log("<<< ------------------------------------------------------------------")
        ;
        
    }

}
