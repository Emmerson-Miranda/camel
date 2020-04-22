package edu.emmerson.camel3.cdi.rmq;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Named;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;
//import org.apache.camel.throttling.ThrottlingInflightRoutePolicy;

@Named("httpRoutePolicy")
public class HttpRoutePolicy  extends RoutePolicySupport  implements CamelContextAware{

	private CamelContext camelContext;
	private AtomicBoolean routeActive = new AtomicBoolean();
	
	public HttpRoutePolicy() {
		System.out.println(this.getClass().getSimpleName() + ":constructor");
		routeActive.set(true);
	}
	
	public boolean isRouteActive() {
		return routeActive.get();
	}

	public void setRouteActive(boolean routeActive) {
		this.routeActive.set(routeActive);
	}
	
	@Override
	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	@Override
	public CamelContext getCamelContext() {
		return this.camelContext;
	}

	@Override
	public void onExchangeBegin(Route route, Exchange exchange) {
		if(!routeActive.get()) {
			exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 503);
			exchange.setException(new HttpException("Service not available temporaly due maintenance tasks."));
		}
		super.onExchangeBegin(route, exchange);
		
	}
	
}
