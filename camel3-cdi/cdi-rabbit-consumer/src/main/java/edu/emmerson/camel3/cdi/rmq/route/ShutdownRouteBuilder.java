package edu.emmerson.camel3.cdi.rmq.route;


import java.time.Instant;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

import edu.emmerson.camel3.cdi.rmq.controlbus.MngtConstants;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/graceful-shutdown.html
 */
public class ShutdownRouteBuilder extends RouteBuilder {
    
	@Inject
	CamelContext context;
	
    @Override
    public void configure() throws Exception {
    	
    	
    	//Handling SIGTERM gracefully
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		public void run() {
    	    	System.out.println("************************************************");
    	    	System.out.println("************************************************");
    	    	System.out.println("Start Exiting Camel Gracefully");
    	    	System.out.println("************************************************");
    	    	System.out.println("************************************************");		
    	    	context.shutdown();
    	    	System.out.println("************************************************");
    	    	System.out.println("************************************************");
    	    	System.out.println("Exiting Camel Fisnished");
    	    	System.out.println("************************************************");
    	    	System.out.println("************************************************");	
    		}
    	});


    	
		from(MngtConstants.MNGT_SHUTDOWN_DIRECT_ENDPOINT)
	        .startupOrder(10000)
	        .routeId(MngtConstants.MNGT_SHUTDOWN_DIRECT_ROUTE_ID)
	        .log("shuting down camel at " + Instant.now().toString())
	        .process().message(m -> { m.getExchange().getContext().shutdown(); } );
    }
    
}
