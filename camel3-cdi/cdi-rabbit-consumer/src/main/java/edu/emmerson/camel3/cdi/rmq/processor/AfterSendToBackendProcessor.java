package edu.emmerson.camel3.cdi.rmq.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AfterSendToBackendProcessor implements Processor {
	
	private static final Logger logger  = LogManager.getLogger(AfterSendToBackendProcessor.class);
	
	private static final String ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER = "ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER";

	@Override
	public void process(Exchange exchange) throws Exception {
		String scounter = (String) exchange.getProperty(ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER);
		int counter = 0;
		if(StringUtils.isEmpty(scounter)) {
			logger.info("UPS something happen ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER was remove from the exchange!!!");
		}else {
			counter = Integer.parseInt(scounter) + 1;
			exchange.setProperty(ROUTE_UPSTREAM_SUCCESSFUL_DELIVERED_COUNTER, counter);
		}
		logger.info("Message delivered! - Counter " + counter);
	}
	
}
