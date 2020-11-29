package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class ErrorRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + ErrorRoute.class.getSimpleName();

	public static final String ROUTE_ID = ErrorRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {

        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .throwException(new Exception("Exception thrown by the route."))
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
