package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class InvalidNumberOnExceptionRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + InvalidNumberOnExceptionRoute.class.getSimpleName();

	public static final String ROUTE_ID = InvalidNumberOnExceptionRoute.class.getSimpleName();

	
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
