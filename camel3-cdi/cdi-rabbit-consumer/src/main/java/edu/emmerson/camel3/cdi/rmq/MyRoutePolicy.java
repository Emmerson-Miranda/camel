package edu.emmerson.camel3.cdi.rmq;

import javax.inject.Named;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;
//import org.apache.camel.throttling.ThrottlingInflightRoutePolicy;

@Named("myRoutePolicy")
public class MyRoutePolicy  extends RoutePolicySupport  implements CamelContextAware{

	private CamelContext camelContext;
	
	public MyRoutePolicy() {
		System.out.println(this.getClass().getSimpleName() + ":constructor");
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
	public void onInit(Route route) {
		System.out.println(this.getClass().getSimpleName() + ":onInit:::" + route.getId());
		System.out.println(route.getProperties());
		super.onInit(route);
	}

	@Override
	public void onRemove(Route route) {
		System.out.println(this.getClass().getSimpleName() + ":onRemove:::" + route.getId());
		System.out.println(route.getProperties());
		super.onRemove(route);
	}

	@Override
	public void onStart(Route route) {
		System.out.println(this.getClass().getSimpleName() + ":onStart:::" + route.getId());
		System.out.println(route.getProperties());
		super.onStart(route);
	}

	@Override
	public void onStop(Route route) {
		System.out.println(this.getClass().getSimpleName() + ":onStop:::" + route.getId());
		System.out.println(route.getProperties());
		super.onStop(route);
	}

	@Override
	public void onSuspend(Route route) {
		System.out.println(this.getClass().getSimpleName() + ":onSuspend:::" + route.getId());
		System.out.println(route.getProperties());
		super.onSuspend(route);
	}

	@Override
	public void onResume(Route route) {
		System.out.println(this.getClass().getSimpleName() + ":onResume:::" + route.getId());
		System.out.println(route.getProperties());
		super.onResume(route);
	}

	@Override
	public void onExchangeBegin(Route route, Exchange exchange) {
		exchange.setProperty("routeActive", Boolean.FALSE);
		System.out.println(this.getClass().getSimpleName() + ":onExchangeBegin:::" + route.getId());
		System.out.println(exchange.getProperties());
		super.onExchangeBegin(route, exchange);
	}

	@Override
	public void onExchangeDone(Route route, Exchange exchange) {
		System.out.println(this.getClass().getSimpleName() + ":onExchangeDone:::" + route.getId());
		System.out.println(exchange.getProperties());
		super.onExchangeDone(route, exchange);
	}

	
	
}
