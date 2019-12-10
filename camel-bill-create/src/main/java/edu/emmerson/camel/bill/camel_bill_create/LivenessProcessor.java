package edu.emmerson.camel.bill.camel_bill_create;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;

public class LivenessProcessor {

	public void processExchange(Exchange exchange) {
		String msg = EnvVars.checkRequiredEnvVars();
		
		if(!StringUtils.isEmpty(msg)) {
			throw new RuntimeException(msg);
		} else {
			exchange.getIn().setBody("<html><body>" + msg + "</body></html>");
		}
	}
}
