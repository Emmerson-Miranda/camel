package edu.emmerson.camel3.cdi.rmq;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/exception-clause.html
 *
 */
public class ConsumerRouteBuilder extends RouteBuilder {

    public static final String RABBITMQ_ROUTING_KEY = "rabbit.consumer";
	
    
    public static String getQueueEndpoint() {
    	StringBuilder sbConsumer = new StringBuilder();
        sbConsumer.append("rabbitmq:myexchange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&queue=myqueue")
        .append("&routingKey=main")
        .append("&durable=true")
        .append("&autoDelete=false")
        .append("&automaticRecoveryEnabled=true")
        .append("&exclusive=false")
        .append("&autoAck=false")
        .append("&concurrentConsumers=2")
        .append("&prefetchCount=2")
        .append("&prefetchEnabled=true");
        return sbConsumer.toString();
    }
    
    public static String getDLQEndpoint() {
    	StringBuilder sbConsumer = new StringBuilder();
        sbConsumer.append("rabbitmq:myexchange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&queue=myqueueDLQ")
        .append("&durable=true")
        .append("&autoDelete=false")
        .append("&automaticRecoveryEnabled=true")
        .append("&exchangePattern=InOnly")
        .append("&routingKey=dlq")
        .append("&exclusive=false")
        .append("&autoAck=false") ;
        return sbConsumer.toString();
    }
    
	@Override
    public void configure() throws Exception {

        //
        //error handling
        //
        onException(Exception.class)
	        .maximumRedeliveries(2)
	        .handled(true)
	        .asyncDelayedRedelivery().redeliveryDelay(1000)
	        .log("\"Error reported: ${exception.message} - cannot process this message.\" - retry ${headers.rabbitmq.DELIVERY_TAG}")
	        .setHeader(RabbitMQConstants.ROUTING_KEY, constant("dlq"))
	        //sending the message to DLQ
	        .toD(getDLQEndpoint())
	    	.process().message(m -> {
	    		MngtProducerRouteBuilder.buildMngtMessage(m, ConsumerConstants.CONSUMER_ROUTE_ID, true, RABBITMQ_ROUTING_KEY);
	        })
	    	//notifiy all consumers to stop
	    	.to(MngtProducerRouteBuilder.DIRECT_PUBLISH_MESSAGE_MNGT_PRODUCER_ENDPOINT)
    	;
        
        
        MetricsRoutePolicy mrp = new MetricsRoutePolicy();
		mrp.setNamePattern(ConsumerConstants.CONSUMER_ROUTE_ID);
		mrp.setUseJmx(true);
		mrp.setJmxDomain(ConsumerConstants.JMX_DOMAIN_NAME);
		
		
        //
        //consuming messages
        //
        from(getQueueEndpoint())
	        .routeId(ConsumerConstants.CONSUMER_ROUTE_ID)
	        .routePolicy(mrp)
		    .to("direct:target");
	    
        //
        //processing message
        //
        from("direct:target")
	        .routeId("ConsumerRouteBuilder-target-routeId")
	        .log(">>> ...........................................................")
	        .log("Message to be sent: ${body}")
	        .log("Message to be sent: ${headers}")
	        .choice()
	        	.when(header("test-scenario").isEqualTo("ko"))
	        		.log("Simulating an exception")
		        	.process((m) -> {
		        		throw new Exception("Some error happen!");
		        	})
	        	.otherwise()
	        		.log("Congrats, message well processed!")
	        .end()
	
	        .log("<<< ...........................................................")
        ;
    }

}
