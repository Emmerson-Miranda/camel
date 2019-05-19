package edu.emmerson.camel.java8.activemq.camel_java8_activemq;

import java.util.UUID;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;

/**
 * A Camel Java8 DSL Router
 */
public class MyActiveMQProducerRoute extends RouteBuilder {
	
	
	public void configure() {
		
		checkActiveMQComponent("activemqproducer");
		
		from("timer:simple?period={{publisher.timer.period.millis}}")
		.routeId(this.getClass().getSimpleName())
		.process()
			.message(m -> {
				m.setHeader("requestId", UUID.randomUUID().toString());
				m.setHeader("type", Math.random() * 10 + 1);
			})
		.transform()
			.message(this::randomBody)
		.log("Producer: ${body}")
		.to("activemqproducer:{{activemq.queue}}?username={{activemq.usr}}&password={{activemq.pwd}}");
	}
	
	
	private String randomBody(Message m) {
		return "<msg>" + m.getHeader("requestId", String.class) + " generated at " + (new java.util.Date()).toString() + "</msg>" ;
	}

	public void checkActiveMQComponent(String name) {
		
		if(this.getContext().getComponent(name) == null) {
			System.out.println( this.getClass().getSimpleName() + ": " + name + " component does not exist, adding a new one.");
			
			String connectionString = System.getProperty("activemq.connectionString");
			
			System.out.println(this.getClass().getSimpleName() + ": Connection String : " + connectionString);
			
			this.getContext().addComponent(name, ActiveMQComponent.activeMQComponent(connectionString));
		}else {
			System.out.println(this.getClass().getSimpleName() + ": reusing "+ name + " component.");
		}
		
	}

}
