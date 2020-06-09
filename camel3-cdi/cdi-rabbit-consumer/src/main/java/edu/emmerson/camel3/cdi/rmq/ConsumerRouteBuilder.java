package edu.emmerson.camel3.cdi.rmq;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/exception-clause.html
 *
 */
public class ConsumerRouteBuilder extends RouteBuilder {

    public static final String RABBITMQ_ROUTING_KEY = "rabbit.consumer";
	
	@Override
    public void configure() throws Exception {
		
		boolean disableSuspension = isDisableSuspensionEnabled();
		
		long lpts = getProcessTimeSimulationMs();
		
        //
        //error handling
        //
        onException(Throwable.class)
	        .maximumRedeliveries(2)
	        .handled(true)
	        .log("onException: ${exception.message}")
	        .log("onException: ${exception}")
	        .asyncDelayedRedelivery().redeliveryDelay(1000)
	        .onExceptionOccurred(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getProperties().forEach((k,v) -> {
						System.out.println("onExceptionOccurred:property:" + k + ":" + v);
					});
					exchange.getIn().getHeaders().forEach((k,v) -> {
						System.out.println("onExceptionOccurred:header:" + k + ":" + v);
					});
				}
	        	
	        })
	        .onRedelivery(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					/*
					System.out.println("redelivery");
					System.out.println("Retry counter " + exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER));
					System.out.println("Retry Max counter " + exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER));
					//System.out.println(exchange.getProperties().get("CamelExceptionCaught"));
					System.out.println(exchange.getProperties().get("CamelToEndpoint"));
					
					HttpOperationFailedException ex  = (HttpOperationFailedException) exchange.getProperties().get("CamelExceptionCaught");
					System.out.println(ex.getStatusCode());
					System.out.println(ex.getStatusText());
					System.out.println(ex.getMessage());
					*/
					exchange.getProperties().forEach((k,v) -> {
						System.out.println("onRedelivery:property:" + k + ":" + v);
					});
					exchange.getIn().getHeaders().forEach((k,v) -> {
						System.out.println("onRedelivery:header:" + k + ":" + v);
					});
				}
	        	
	        }
	        )
	        //.log("\"Error reported: ${exception.message} - cannot process this message.\" - retry ${headers.rabbitmq.DELIVERY_TAG}")
	        .setHeader(RabbitMQConstants.ROUTING_KEY, constant("dlq"))
	        //sending the message to DLQ
	        .toD(getDLQEndpoint())
	        .choice()
	        	.when(constant(disableSuspension).isEqualTo(false))
	        		.log("Stop consumers enabled - stopping them")
			    	.process().message(m -> {
			    		int restartDelayInMilis = 30000;
			    		LinkedHashMap<String, String> body = MngtProducerRouteBuilder.buildMngtMessage(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID, true, RABBITMQ_ROUTING_KEY, restartDelayInMilis);
			    		m.setBody(body);
			        })
			    	.to(MngtConstants.MNGT_PRODUCER_DIRECT_ENDPOINT)
			    .otherwise()
			    	.log("Stop consumers disabled by DISABLE_SUSPENSION environment variable.")
			.end()
    	;
        
        
        MetricsRoutePolicy mrp = MetricsFactory.createMetricsRoutePolicy(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID);
		
        //
        //consuming messages
        //
        from(getQueueEndpoint())
	        .routeId(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID)
	        .routePolicy(mrp)
		    .to("direct:target");
	    
        //
        //processing message
        //
        from("direct:target")
	        .routeId(ConsumerConstants.CONSUMER_DIRECT_ROUTE_ID)
	        .log(">>> ...........................................................")
	        .log("Message to be sent: ${header.X-Correlation-ID}")
	        .choice()
	        	.when(header("test-scenario").isEqualTo("ko"))
		        	.process((m) -> {
		        		throw new Exception("Some error happen!");
		        	})
	        .end()
	        .process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println("Start delay.");
					Thread.sleep(lpts);
					System.out.println("Finish delay.");
				}
	        	
	        })
	        .to(getUpstreamEndpoint())
	        .log("<<< ...........................................................")
	        ;
        ;
    }


    private String getQueueEndpoint() {
    	StringBuilder sbConsumer = new StringBuilder();
    	
    	String envvar = System.getenv("RMQ_CONSUMER_QUEUE_CS");
    	if(StringUtils.isEmpty(envvar)) {
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
	        .append("&prefetchEnabled=true")
	        .append("&transferException=true")
	        ;
    	}else {
    		sbConsumer.append(envvar);
    	}
    	
    	System.out.println("CONFIGURATION -> RMQ_CONSUMER_QUEUE_CS=" + sbConsumer.toString());
        return sbConsumer.toString();
    }
    
    private String getDLQEndpoint() {
    	StringBuilder sbConsumer = new StringBuilder();
    	
    	String envvar = System.getenv("RMQ_CONSUMER_DLQ_CS");
    	if(StringUtils.isEmpty(envvar)) {
	        sbConsumer.append("rabbitmq:myexchange?")
	        .append("connectionFactory=#consumerConnectionFactoryService")
	        .append("&queue=myqueueDLQ")
	        .append("&durable=true")
	        .append("&autoDelete=false")
	        .append("&automaticRecoveryEnabled=true")
	        .append("&exchangePattern=InOnly")
	        .append("&routingKey=dlq")
	        .append("&exclusive=false")
	        .append("&autoAck=false")
	        .append("&transferException=true")
	        ;
    	}else {
    		sbConsumer.append(envvar);
    	}
    	
    	System.out.println("CONFIGURATION -> RMQ_CONSUMER_DLQ_CS=" + sbConsumer.toString());
        return sbConsumer.toString();
    }
    
    private String getUpstreamEndpoint() {
    	StringBuilder sbConsumer = new StringBuilder();
    	
    	String envvar = System.getenv("RMQ_UPSTREAM_CS");
    	if(StringUtils.isEmpty(envvar)) {
    		sbConsumer.append("undertow:http://upstream:10003/microservice/myservice?httpMethodRestrict=POST&exchangePattern=InOut");
    	}else {
    		sbConsumer.append(envvar);
    	}
    	
    	System.out.println("CONFIGURATION -> RMQ_UPSTREAM_CS=" + sbConsumer.toString());
        return sbConsumer.toString();
    }
    
    private long getProcessTimeSimulationMs() {
    	String pts = System.getenv("PROCESS_TIME_SIMULATION_MS");
		long lpts = 300;
		try {
			lpts = Long.parseLong(pts);
		}catch(NumberFormatException e) {
			lpts = 300;
		}
		System.out.println("CONFIGURATION -> PROCESS_TIME_SIMULATION_MS=" + lpts);
		return lpts;
    }
    
    private boolean isDisableSuspensionEnabled() {
    	boolean disableSuspension = "true".equals(System.getenv("DISABLE_SUSPENSION"));
		System.out.println("CONFIGURATION -> DISABLE_SUSPENSION=" + disableSuspension);
		return disableSuspension;
    }
    
}
