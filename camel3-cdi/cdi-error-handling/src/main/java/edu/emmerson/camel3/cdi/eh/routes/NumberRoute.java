package edu.emmerson.camel3.cdi.eh.routes;

import java.util.LinkedHashMap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.lang3.StringUtils;

import edu.emmerson.camel3.cdi.eh.EHPojo;

public class NumberRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + NumberRoute.class.getSimpleName();

	public static final String ROUTE_ID = NumberRoute.class.getSimpleName();

	@Override
	public void configure() throws Exception {
		
		errorHandler(noErrorHandler()); //if you don't do this the exception is not going to be catch by the caller route
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .unmarshal().json(JsonLibrary.Jackson)
        .process( e -> {
        	@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> o = e.getIn().getBody(LinkedHashMap.class);
        	String val = o.get("value");
        	Integer i = Integer.valueOf(val);
        	
        	EHPojo p = new EHPojo(i.intValue(), ROUTE_ID +  " Integer succesfully read (FromRouteId: " + e.getFromRouteId() + ").");
        	
        	
        	String xsleep = e.getIn().getHeader("x-sleep", String.class) ;
        	long sleep = 0;
        	try{
        		sleep = StringUtils.isEmpty(xsleep) ? 0 : Long.parseLong(xsleep);
        	} catch (NumberFormatException nfe) {
        		System.out.println(nfe.getMessage());
        	}
        	System.out.println("Sleeping miliseconds: " + sleep);
        	Thread.sleep(sleep);
        	
        	e.getIn().setBody(p);
        })
        .marshal().json(JsonLibrary.Jackson)
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
