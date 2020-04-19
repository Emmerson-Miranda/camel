package edu.emmerson.camel3.cdi.rmq;


import java.time.Instant;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/graceful-shutdown.html
 */
public class ShutdownRouteBuilder extends RouteBuilder {

	private static final String DIRECT_SHUTDOWN = "direct:shutdown";
public static final String DIRECT_SHUTDOWN_MESSAGE_CONSUMERS_ENDPOINT = DIRECT_SHUTDOWN;
	
	public ShutdownRouteBuilder() {
		System.out.println("--------------- new instance --------- " + this.getClass().getName());
	}
    
    @Override
    public void configure() throws Exception {

		from(DIRECT_SHUTDOWN)
        	.setProperty("StartingDate", constant(Instant.now().toString())).routePolicyRef("myRoutePolicy")
	        .startupOrder(10000)
	        .routeId("shutdown-routeid")
	        .log("shuting down camel")
	        .process().message(m -> {
	        	final CamelContext ctx = m.getExchange().getContext();
	        	ctx.getRoutes().forEach(r ->{
	        		/*
        			if(
        					(r.getEndpoint() instanceof org.apache.camel.component.rest.RestEndpoint)
        					||
        					(r.getEndpoint() instanceof org.apache.camel.component.rest.RestApiEndpoint)
        			) {
        				
        				ServiceHelper.stopAndShutdownServices(r.getConsumer());
        				ServiceHelper.startService(r.getConsumer());	
        			}
	        		*/
	        	});

	        	ctx.shutdown();
	        }
	        )
	        ;
        
    }
    
}
