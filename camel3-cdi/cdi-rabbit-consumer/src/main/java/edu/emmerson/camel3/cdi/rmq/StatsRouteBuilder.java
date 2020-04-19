package edu.emmerson.camel3.cdi.rmq;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.component.rest.RestApiEndpoint;
import org.apache.camel.component.rest.RestEndpoint;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/graceful-shutdown.html
 */
public class StatsRouteBuilder extends RouteBuilder {

	public static final String DIRECT_STATS = "direct:stats";

	public StatsRouteBuilder()  throws Exception{
		System.out.println("--------------- new instance --------- " + this.getClass().getName());
	}

	@Override
	public void configure() throws Exception {
        
		MyRoutePolicy crp = new MyRoutePolicy();
		
		MetricsRoutePolicy mrp = new MetricsRoutePolicy();
		mrp.setNamePattern(ConsumerConstants.STATUS_ROUTE_ID);
		mrp.setUseJmx(true);
		mrp.setJmxDomain(ConsumerConstants.JMX_DOMAIN_NAME);
		
		from(DIRECT_STATS)
			.routePolicy(mrp, crp)
			.routeId(ConsumerConstants.STATUS_ROUTE_ID)
			.log("starting route status camel: ${exchangeProperty.routeActive}")
			.choice()
				.when(simple("${exchangeProperty.routeActive} == false"))
					.log("The route is inactive")
					.setBody(constant("Not allowed"))
				.otherwise()
					.process().message(m -> {
						final CamelContext ctx = m.getExchange().getContext();
						final List<String> routes = new ArrayList<String>();
						ctx.getRoutes().forEach(r -> {
							/*
			        		System.out.println("```");
			        		System.out.println(r.getClass().getName() + ":::" + r.getId());
			        		System.out.println(r.getEndpoint().getClass().getName() + ":::" + r.getEndpoint());
			        		System.out.println(r.getConsumer().getClass().getName() + ":::" + r.getConsumer());
			        		System.out.println(r.getProperties());
			        		System.out.println("```");
			        		*/
							if ((r.getEndpoint() instanceof RestEndpoint)
									|| (r.getEndpoint() instanceof RestApiEndpoint)) {
								routes.add(r.getId());
							}
		
						});
						m.getExchange().setProperty("routesIds", routes);
						m.getExchange().setProperty("routesSize", routes.size());
						
					})
					.loop(simple("${exchangeProperty.routesSize}"))
						.log("route id ${exchangeProperty.CamelLoopIndex} - ${exchangeProperty.routesIds[${exchangeProperty.CamelLoopIndex}]}")
						.toD("controlbus:route?routeId=${exchangeProperty.routesIds[${exchangeProperty.CamelLoopIndex}]}&action=stats")
			
			;

	}

}
