package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class InvalidNumberTryCatchRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + InvalidNumberTryCatchRoute.class.getSimpleName();

	public static final String ROUTE_ID = InvalidNumberTryCatchRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .doTry()
        	.to(NumberRoute.DIRECT)
        .doCatch(java.lang.NumberFormatException.class)
        	.setBody(constant(ROUTE_ID + ": Invalid number from doTry block"))
        .endDoTry()
        .log("Route End :: ${exchangeId} :: ${routeId}");
		
	}

}
