package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class InvalidNumberTryCatchRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + InvalidNumberTryCatchRoute.class.getSimpleName();

	public static final String ROUTE_ID = InvalidNumberTryCatchRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {
		
    	onException(Throwable.class)
    	.handled(true)
		.log("onException:: ${header.X-Correlation-ID} :: ${exception.message} :: ${exception}")
		.setBody(constant("Error handling by " + ROUTE_ID))
		;
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .doTry()
        	.to(InvalidNumberRoute.DIRECT)
        .doCatch(java.lang.NumberFormatException.class)
        	.setBody(constant(ROUTE_ID + ": Invalid number from doTry block"))
        .endDoTry();
		
	}

}
