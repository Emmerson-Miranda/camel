package edu.emmerson.camel3.cdi.rmq;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/exception-clause.html
 *
 */
public class ConsumerRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //
    	//connection string
    	//
        StringBuilder sbConsumer = new StringBuilder();
        sbConsumer.append("rabbitmq:myexchange?")
        .append("connectionFactory=#consumerConnectionFactoryService")
        .append("&queue=myqueue")
        .append("&durable=true")
        .append("&autoDelete=false")
        .append("&automaticRecoveryEnabled=true")
        .append("&deadLetterExchange=myexchangeDLQ")
        .append("&deadLetterExchangeType=direct")
        .append("&deadLetterQueue=myqueueDLQ")
        
        .append("&exclusive=false")
        .append("&autoAck=false")
        .append("&concurrentConsumers=2")
        .append("&prefetchCount=2")
        .append("&prefetchEnabled=true");
          
        //
        //retries
        //
        onException(Exception.class)
        .useOriginalMessage()
        .maximumRedeliveries(4)
        .asyncDelayedRedelivery().redeliveryDelay(3000)
    	.log("handling the exception - global - retry ${headers.rabbitmq.DELIVERY_TAG}")
    	.choice()
    		.when(header(RabbitMQConstants.DELIVERY_TAG).isEqualTo("2"))
    			.log("third attempt, moving to DLQ")
    		.otherwise()
    			.log("Back to the queue, continue retrying!")
    			.setHeader(RabbitMQConstants.REQUEUE, constant(true))
    	.end();
        
        //
        //consuming messages
        //
        from(sbConsumer.toString())
        .routeId(ConsumerRouteBuilder.class.getName() + "-Consumer")
	    .to("direct:target");
	    
        //
        //processing message
        //
        from("direct:target")
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
        .end();
    }

}
