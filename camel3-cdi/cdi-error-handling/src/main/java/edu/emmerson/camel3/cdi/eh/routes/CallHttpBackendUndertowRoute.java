package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class CallHttpBackendUndertowRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + CallHttpBackendUndertowRoute.class.getSimpleName();

	public static final String ROUTE_ID = CallHttpBackendUndertowRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {

    	onException(Throwable.class)
    	.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("Throwable Error handling by " + ROUTE_ID);
		;
		
		onException(org.apache.camel.http.base.HttpOperationFailedException.class)
    	.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("HttpOperationFailedException Error handling by " + ROUTE_ID);
		;
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .toD("undertow:http://0.0.0.0:8080/eh/in?exchangePattern=InOut")
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
