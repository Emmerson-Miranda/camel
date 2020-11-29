package edu.emmerson.camel3.cdi.eh.routes;

import java.util.LinkedHashMap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import edu.emmerson.camel3.cdi.eh.EHPojo;

public class InvalidNumberRoute  extends RouteBuilder {
	
	public static final String DIRECT = "direct:" + InvalidNumberRoute.class.getSimpleName();

	public static final String ROUTE_ID = InvalidNumberRoute.class.getSimpleName();

	@Override
	public void configure() throws Exception {
		
        from(DIRECT)
        .routeId(ROUTE_ID)
        .unmarshal().json(JsonLibrary.Jackson)
        .process( exchange -> {
        	@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> o = exchange.getIn().getBody(LinkedHashMap.class);
        	String val = o.get("value");
        	Integer i = Integer.valueOf(val);
        	EHPojo p = new EHPojo(i.intValue(), ROUTE_ID +  " Integer succesfully read.");
        	
        	exchange.getIn().setBody(p);
        })
        .marshal().json(JsonLibrary.Jackson)
        ;
		
	}

}
