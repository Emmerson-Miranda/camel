package edu.emmerson.camel3.cdi.rmq;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.component.rest.RestApiEndpoint;
import org.apache.camel.component.rest.RestEndpoint;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/components/latest/controlbus-component.html
 *
 */
public class MngtConsumerRouteBuilder extends RouteBuilder {
	
	public static final String MNGT_QUEUE_NAME = "cdi-rabbit-consumer-mngt-consumer-" + UUID.randomUUID();

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

    	MetricsRoutePolicy mrp = MetricsFactory.createMetricsRoutePolicy(MngtConstants.MNGT_CONSUMER_RABBITMQ_ROUTE_ID);	
		
        //
        //consuming management messages
        //
        from(getQueueEndpoint())
	        .routeId(MngtConstants.MNGT_CONSUMER_RABBITMQ_ROUTE_ID)
	        .routePolicy(mrp)
		    .to(MngtConstants.MNGT_CONSUMER_DIRECT_ENDPOINT);
	    
        
        //
        //processing message
        //	
        from(MngtConstants.MNGT_CONSUMER_DIRECT_ENDPOINT)
	        .routeId(MngtConstants.MNGT_CONSUMER_DIRECT_ROUTE_ID)
	        .log(">>> ------------------------------------------------------------------")
	        .log("Camel management controlbus action to process: ${body}")
	        .process().message(m -> {
	        	
	        	@SuppressWarnings("unchecked")
				final LinkedHashMap<String, String> body = (LinkedHashMap<String, String>) m.getBody();
				
				final String routeId = body.get("routeId");
				String action = "true".equals(body.get("suspend")) ? "suspend" : "resume";
				String rd = body.get("restartDelay");
				int restartDelay = (StringUtils.isEmpty(rd) ? 0 : Integer.parseInt(rd));
				
				Route r = m.getExchange().getContext().getRoute(routeId);
				if ((r.getEndpoint() instanceof RestEndpoint) || (r.getEndpoint() instanceof RestApiEndpoint)) {
					action = "suspend".equalsIgnoreCase(action) ? "stop" : "start";
				}
				
				if(restartDelay > 0 && "suspend".equalsIgnoreCase(action)) {
					action = "restart";
				}

				m.getExchange().setProperty("route-id", routeId);
				m.getExchange().setProperty("route-action", action);
				m.getExchange().setProperty("route-restart-delay", restartDelay);


	        })
	        //.toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=stop")
	        //.toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=restart&restartDelay=10000")
	        //.log("Camel management controlbus ${exchangeProperty.route-id} to be ${exchangeProperty.route-action} for ${exchangeProperty.route-restart-delay} miliseconds!")
	        .choice()
	        	.when( exchangeProperty("route-restart-delay").isGreaterThan(0) )
	        		.log("Camel management controlbus ${exchangeProperty.route-id} to be ${exchangeProperty.route-action} for ${exchangeProperty.route-restart-delay} miliseconds!")
	        		.toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=${exchangeProperty.route-action}&restartDelay=${exchangeProperty.route-restart-delay}")
	        		.choice()
        				.when( exchangeProperty("route-action").isEqualTo("stop") )
        					.log("Camel management controlbus configuring restart")
        					.toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=restart&restartDelay=${exchangeProperty.route-restart-delay}")
        				.otherwise()
        					.log("Camel management controlbus configuring restart finished!")
        			.endChoice()
	        	.otherwise()
	        		.log("Camel management controlbus ${exchangeProperty.route-id} to be ${exchangeProperty.route-action} till further notice!")
	        		.toD("controlbus:route?routeId=${exchangeProperty.route-id}&action=${exchangeProperty.route-action}")
        	.end()
        
	        .log("<<< ------------------------------------------------------------------")
        ;
        
    }

}
