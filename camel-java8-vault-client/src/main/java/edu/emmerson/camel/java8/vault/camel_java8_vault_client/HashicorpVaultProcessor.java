package edu.emmerson.camel.java8.vault.camel_java8_vault_client;

import java.util.LinkedHashMap;
import org.apache.camel.Exchange;

/**
 * Transform Hashicorp response format to other format and update the body.
 * @author Emmerson
 *
 */
public class HashicorpVaultProcessor {

	public void processExchange(Exchange exchange) {		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> m = exchange.getIn().getBody(LinkedHashMap.class);

		LinkedHashMap<String, Object> lhm = new LinkedHashMap<String, Object>();
		lhm.put("keyname", exchange.getProperty("keyname"));
		lhm.put("value", m.get("data"));
		exchange.getIn().setBody(lhm);

	}

}
