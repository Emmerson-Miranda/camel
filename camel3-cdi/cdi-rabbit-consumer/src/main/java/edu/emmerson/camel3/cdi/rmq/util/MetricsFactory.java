package edu.emmerson.camel3.cdi.rmq.util;

import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;

import edu.emmerson.camel3.cdi.rmq.controlbus.MngtConstants;

public final class MetricsFactory {
	
	public static MetricsRoutePolicy createMetricsRoutePolicy (String namePattern) {
		MetricsRoutePolicy mrp = new MetricsRoutePolicy();
		mrp.setNamePattern(namePattern);
		mrp.setUseJmx(true);
		mrp.setJmxDomain(MngtConstants.JMX_DOMAIN_NAME);
		return mrp;
	}
	
}
