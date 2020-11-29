package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class ErrorOnExceptionToTryCatchRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + ErrorOnExceptionToTryCatchRoute.class.getSimpleName();

	public static final String ROUTE_ID = ErrorOnExceptionToTryCatchRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .doTry()
        	.to(ErrorRoute.DIRECT)
        .doCatch(Throwable.class)
        	.transform().constant(ROUTE_ID + ": Invalid number from doTry block")
        .endDoTry()
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
