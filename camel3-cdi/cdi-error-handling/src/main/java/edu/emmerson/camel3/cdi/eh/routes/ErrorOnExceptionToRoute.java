package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class ErrorOnExceptionToRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + ErrorOnExceptionToRoute.class.getSimpleName();

	public static final String ROUTE_ID = ErrorOnExceptionToRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {

    	onException(Throwable.class)
    	.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("Error handling by " + ROUTE_ID);
		;
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .to(ErrorRoute.DIRECT)
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
