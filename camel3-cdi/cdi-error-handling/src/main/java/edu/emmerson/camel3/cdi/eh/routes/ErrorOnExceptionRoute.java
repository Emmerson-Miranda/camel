package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class ErrorOnExceptionRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + ErrorOnExceptionRoute.class.getSimpleName();

	public static final String ROUTE_ID = ErrorOnExceptionRoute.class.getSimpleName();

	
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
        .throwException(new Exception("Exception thrown by the route."))
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
