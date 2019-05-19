package edu.emmerson.camel.java8.activemq.camel_java8_activemq;

import java.util.logging.Logger;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;

/**
 * A Camel Java8 DSL Router
 */
public class MyActiveMQConsumerRoute extends RouteBuilder {
	
	static Logger log = Logger.getLogger(MyActiveMQConsumerRoute.class.getName());
	
	public void configure() {
		
		checkActiveMQComponent("activemqconsumer");

		from("activemqconsumer:{{activemq.queue}}?username={{activemq.usr}}&password={{activemq.pwd}}")
			.routeId(this.getClass().getSimpleName())
			.log("Consumer: ${body}")
			.log("Consumer: ${headers}");
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
