package edu.emmerson.camel.bill.camel_bill_compute_discount;

import org.apache.camel.Exchange;

public class ReadinessProcessor {

	public void processExchange(Exchange exchange) {
		exchange.getIn().setBody("<html><body>OK</body></html>");
	}
	
}
