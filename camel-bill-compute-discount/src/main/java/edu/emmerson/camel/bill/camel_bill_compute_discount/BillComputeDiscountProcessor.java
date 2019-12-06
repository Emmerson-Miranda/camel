package edu.emmerson.camel.bill.camel_bill_compute_discount;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import org.apache.camel.Exchange;

public class BillComputeDiscountProcessor {

	public void processExchange(Exchange exchange) {
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> m = exchange.getIn().getBody(LinkedHashMap.class);
		
		String type = (String) m.get("customerType");
		String samount = (String) m.get("amount");
		BigDecimal amount = new BigDecimal(samount);
		
		double damount = amount.doubleValue();
		double discount = 0;
		
		if(damount > 0 && damount < 100) {
			discount = 0;
		} else if(damount > 99 && damount < 200) {
			discount = -10;
		} else {
			discount = -20;
		}
		
		BigDecimal finalamount = amount.add(new BigDecimal(discount));
		
		LinkedHashMap<String, Object> lhm = new LinkedHashMap<String, Object>();
		lhm.put("customerType", type);
		lhm.put("amount", amount);
		lhm.put("amountAfterDiscount", finalamount.toString());
		exchange.getIn().setBody(lhm);

	}
	
}
