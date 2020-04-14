package edu.emmerson.camel3.cdi.rmq;


import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/graceful-shutdown.html
 */
public class ShutdownRouteBuilder extends RouteBuilder {

	public static final String DIRECT_SHUTDOWN_MESSAGE_CONSUMERS_ENDPOINT = "direct:shutdown";
    
    @Override
    public void configure() throws Exception {

        from("direct:shutdown")
	        .startupOrder(10000)
	        .routeId("shutdown-routeid")
	        .log("shuting down camel")
	        .process().message(m -> {
	        	final CamelContext ctx = m.getExchange().getContext();
	        	ctx.getShutdownStrategy().setSuppressLoggingOnTimeout(true);
	        	ctx.getShutdownStrategy().setLogInflightExchangesOnTimeout(false);
	        	ctx.getShutdownStrategy().setTimeout(30);
	        	ctx.shutdown();
	        }
	        );
        
    }
    
}
