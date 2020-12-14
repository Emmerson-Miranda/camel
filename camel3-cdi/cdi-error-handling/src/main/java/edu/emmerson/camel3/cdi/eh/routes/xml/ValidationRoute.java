package edu.emmerson.camel3.cdi.eh.routes.xml;

import org.apache.camel.builder.RouteBuilder;

public class ValidationRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + ValidationRoute.class.getSimpleName();

	public static final String ROUTE_ID = ValidationRoute.class.getSimpleName();

	@Override
	public void configure() throws Exception {
		
		onException(org.apache.camel.support.processor.validation.SchemaValidationException.class)
		.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("Validation Error handling by " + ROUTE_ID);
		;
		
		onException(org.apache.camel.FailedToCreateProducerException.class)
		.handled(true)
    	.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
    	.transform()
    		.constant("FailedToCreateProducerException Error handling by " + ROUTE_ID);
		;
	    
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .toD("validator:schemas/${header.x-schema}")
        .log("Body :: ${body}")
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
