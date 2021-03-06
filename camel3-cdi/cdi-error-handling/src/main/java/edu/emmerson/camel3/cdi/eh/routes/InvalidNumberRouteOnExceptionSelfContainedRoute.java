package edu.emmerson.camel3.cdi.eh.routes;

import java.util.LinkedHashMap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import edu.emmerson.camel3.cdi.eh.EHPojo;

public class InvalidNumberRouteOnExceptionSelfContainedRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + InvalidNumberRouteOnExceptionSelfContainedRoute.class.getSimpleName();

	public static final String ROUTE_ID = InvalidNumberRouteOnExceptionSelfContainedRoute.class.getSimpleName();

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
        .unmarshal().json(JsonLibrary.Jackson)
        .process( exchange -> {
        	@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> o = exchange.getIn().getBody(LinkedHashMap.class);
        	String val = o.get("value");
        	Integer i = Integer.valueOf(val);
        	
        	EHPojo p = new EHPojo(i.intValue(), ROUTE_ID +  " Integer succesfully read (FromRouteId: " + exchange.getFromRouteId() + ").");
        	
        	exchange.getIn().setBody(p);
        })
        .marshal().json(JsonLibrary.Jackson)
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
