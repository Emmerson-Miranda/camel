package edu.emmerson.camel3.cdi.rmq.route;


import java.time.Instant;

import org.apache.camel.builder.RouteBuilder;

import edu.emmerson.camel3.cdi.rmq.controlbus.MngtConstants;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/graceful-shutdown.html
 */
public class ShutdownRouteBuilder extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {

		from(MngtConstants.MNGT_SHUTDOWN_DIRECT_ENDPOINT)
	        .startupOrder(10000)
	        .routeId(MngtConstants.MNGT_SHUTDOWN_DIRECT_ROUTE_ID)
	        .log("shuting down camel at " + Instant.now().toString())
	        .process().message(m -> { m.getExchange().getContext().shutdown(); } );
    }
    
}
