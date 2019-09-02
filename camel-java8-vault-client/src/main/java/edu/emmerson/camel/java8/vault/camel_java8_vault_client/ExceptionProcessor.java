package edu.emmerson.camel.java8.vault.camel_java8_vault_client;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;

/**
 * Generate error message
 * @author Emmerson
 *
 */
public class ExceptionProcessor {

	public void process(Exchange exchange) throws Exception {
		Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		
		LinkedHashMap<String, Object> lhm = new LinkedHashMap<String, Object>();
		lhm.put("keyname", exchange.getProperty("keyname"));
		lhm.put("error", cause.getMessage());
		exchange.getIn().setBody(lhm);
	}

	
}
