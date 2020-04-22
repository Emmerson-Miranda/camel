package edu.emmerson.camel3.cdi.rmq;

import java.time.Instant;
import java.util.LinkedHashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

public class MngtDescribeRouteBuilder extends RouteBuilder {
	

    @Override
    public void configure() throws Exception {

    	from(MngtConstants.MNGT_DESCRIBE_DIRECT_ENDPOINT)
        .routeId(MngtConstants.MNGT_DESCRIBE_DIRECT_ROUTE_ID)
        .process().message(m -> {
        	
			LinkedHashMap<String, String> routes = new LinkedHashMap<String, String>();
			
			LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();
			body.put("date", Instant.now().toString());
			body.put("routes", routes);
			
			final CamelContext ctx = m.getExchange().getContext();
			ctx.getRoutes().forEach(r -> {
				routes.put(r.getId(), r.getEndpoint().getClass().getSimpleName());
			});
			
			m.setBody(body);
			
        })
        ;
    }
    
}
