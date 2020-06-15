package edu.emmerson.camel3.cdi.rmq.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;

public class AfterSendToBackendProcessor implements Processor {
	
	private static final String ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER = "ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER";

	@Override
	public void process(Exchange exchange) throws Exception {
		String scounter = (String) exchange.getProperty(ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER);
		if(StringUtils.isEmpty(scounter)) {
			System.out.println("UPS something happen ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER was remove from the exchange!!!");
		}else {
			int counter = Integer.parseInt(scounter) + 1;
			exchange.setProperty(ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER, counter);
		}
		System.out.println("Message delivered!");
	}
	
}
