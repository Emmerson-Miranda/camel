package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class CallHttpBackendHttpRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + CallHttpBackendHttpRoute.class.getSimpleName();

	public static final String ROUTE_ID = CallHttpBackendHttpRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {

    	onException(Throwable.class)
    	.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("Throwable Error handling by " + ROUTE_ID);
		;
		
		onException(java.net.SocketTimeoutException.class)
    	.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("SocketTimeoutException Error handling by " + ROUTE_ID);
		;
		
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .setHeader(Exchange.HTTP_URI, constant("http://0.0.0.0:8080/eh/in"))
        .to("http://oldhost/?socketTimeout=2000")
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
