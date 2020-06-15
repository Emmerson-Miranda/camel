package edu.emmerson.camel3.cdi.rmq.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;

import edu.emmerson.camel3.cdi.rmq.ConfigReader;

public class BeforeSendToBackendProcessor implements Processor {

	private static final String ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER = "ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER";

	@Override
	public void process(Exchange exchange) throws Exception {
		long lpts = ConfigReader.getProcessTimeSimulationMs();
		
		String scounter = (String) exchange.getProperty(ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER);
		
		if(StringUtils.isEmpty(scounter)) {
			scounter = "0";
			exchange.setProperty(ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER, scounter);
		}
		
		System.out.println("Processing message! - Counter: " + scounter);
		Thread.sleep(lpts);
	}
}
