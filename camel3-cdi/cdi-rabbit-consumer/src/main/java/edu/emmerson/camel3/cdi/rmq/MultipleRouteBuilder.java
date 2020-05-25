package edu.emmerson.camel3.cdi.rmq;

import javax.enterprise.inject.Produces;

import org.apache.camel.builder.RouteBuilder;

/**
 * https://camel.apache.org/manual/latest/rest-dsl.html
 * 
 * @author emmersonmiranda
 *
 */
public class MultipleRouteBuilder {

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
