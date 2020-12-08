package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;

public class NoErrorRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + NoErrorRoute.class.getSimpleName();

	public static final String ROUTE_ID = NoErrorRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {

        from(DIRECT)
        .routeId(ROUTE_ID)
        .log("Route Start :: ${exchangeId} :: ${routeId}")
        .setBody(constant("Happy Path from " + ROUTE_ID))
        .process(e -> {
        	String xsleep = e.getIn().getHeader("x-sleep", String.class) ;
        	long sleep = 0;
        	try{
        		sleep = StringUtils.isEmpty(xsleep) ? 0 : Long.parseLong(xsleep);
        	} catch (NumberFormatException nfe) {
        		System.out.println(nfe.getMessage());
        	}
        	System.out.println("Sleeping miliseconds: " + sleep);
        	Thread.sleep(sleep);
        })
        .log("Route End :: ${exchangeId} :: ${routeId}")
        ;
		
	}

}
