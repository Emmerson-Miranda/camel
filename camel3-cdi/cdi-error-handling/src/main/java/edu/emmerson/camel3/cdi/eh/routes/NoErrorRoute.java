package edu.emmerson.camel3.cdi.eh.routes;

import org.apache.camel.builder.RouteBuilder;

public class NoErrorRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + NoErrorRoute.class.getSimpleName();

	public static final String ROUTE_ID = NoErrorRoute.class.getSimpleName();

	
	@Override
	public void configure() throws Exception {

        from(DIRECT)
        .routeId(ROUTE_ID)
        .setBody(constant("Happy Path from " + ROUTE_ID));
		
	}

}
