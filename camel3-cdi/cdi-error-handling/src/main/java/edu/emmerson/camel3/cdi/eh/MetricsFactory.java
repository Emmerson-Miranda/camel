package edu.emmerson.camel3.cdi.eh;

import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;

public final class MetricsFactory {
	
	private static final String JMX_DOMAIN_NAME = "MyMetrics";
	
	public static MetricsRoutePolicy createMetricsRoutePolicy (String namePattern) {
		MetricsRoutePolicy mrp = new MetricsRoutePolicy();
		mrp.setNamePattern(namePattern);
		mrp.setUseJmx(true);
		mrp.setJmxDomain(JMX_DOMAIN_NAME);
		return mrp;
	}
	
}
