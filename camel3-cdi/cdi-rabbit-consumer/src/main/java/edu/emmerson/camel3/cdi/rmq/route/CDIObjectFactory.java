package edu.emmerson.camel3.cdi.rmq.route;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.builder.RouteBuilder;

import edu.emmerson.camel3.cdi.rmq.util.ConfigReader;

/**
 * https://camel.apache.org/manual/latest/rest-dsl.html
 * 
 * @author emmersonmiranda
 *
 */
public class CDIObjectFactory {

	@ApplicationScoped
	@Named("consumerConnectionFactoryService")
	@Produces
	protected com.rabbitmq.client.ConnectionFactory  createRabbitConnectionFactory() throws Exception {
		com.rabbitmq.client.ConnectionFactory cf = new com.rabbitmq.client.ConnectionFactory();
		cf.setHost(ConfigReader.getRabbitHost());
		cf.setPort(ConfigReader.getRabbitPort());
		cf.setUsername(ConfigReader.getRabbitUsername());
		cf.setPassword(ConfigReader.getRabbitPassword());
		cf.setVirtualHost(ConfigReader.getRabbitVirtualHost());
		return cf;
	}

	@Produces
	protected RouteBuilder createProducerMessage() throws Exception {
	    return new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	        	
	        	rest("/rmq").id("rmq-endpoint").description("RabbitMQ rest service")
	            .post("/publish").id("rmq-publish-resource").description("Publish a message in RabbitMQ")
	                .responseMessage().code(204).message("Message storaged").endResponseMessage() 
	                .to("direct:publishMessage");

	        };
	    };
	}
	
	@Produces
	protected RouteBuilder createRouteBuilderHello() throws Exception {
	    return new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	            rest("/say/hello")
	                .get().route().transform().constant("Hello World");
	        };
	    };
	}
	
	@Produces
	protected RouteBuilder createRouteBuilderGoodbye() throws Exception {
	    return new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	            rest("/say/bye")
	                .get().consumes("application/json").route().transform().constant("Bye World").endRest()
	                .post().to("mock:update");
	        };
	    };
	}
	
	@Produces
	protected RouteBuilder createRouteBuilderDirect() throws Exception {
	    return new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	            from("direct:bye").routeId("myrouteId").transform().constant("Bye World");
	        };
	    };
	}	
	
	
}
