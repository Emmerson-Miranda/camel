package edu.emmerson.camel3.cdi.rmq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
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

		//.throttle(2).timePeriodMillis(10000).rejectExecution(true)
		
		from(DIRECT_STATS)
			.routeId(ConsumerConstants.STATS_DIRECT_ROUTE_ID)
			.log("running route status camel: ${exchangeProperty.routeActive} : " + ConsumerConstants.STATS_DIRECT_ROUTE_ID)

			.process().message(m -> {
				final CamelContext ctx = m.getExchange().getContext();
				final List<String> routes = new ArrayList<String>();
				
				Comparator<Route> compareById = new Comparator<Route>() {
				    @Override
				    public int compare(Route o1, Route o2) {
				        return o1.getId().compareTo(o2.getId());
				    }
				};
				
				List<Route> unordered = ctx.getRoutes();
				Collections.sort(unordered, compareById);
				
				unordered.forEach(r -> {
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
