package edu.emmerson.camel.java8.vault.camel_java8_vault_client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;

/**
 * Transform Hashicorp response format to other format and update the body.
 * @author Emmerson
 *
 */
public class HashicorpVaultProcessor {

	@SuppressWarnings("unchecked")
	public void processExchange(Exchange exchange) {
		Object b = exchange.getIn().getBody();

		if (b instanceof Map) {
			Map<String, Object> m = (Map<String, Object>) b;

			LinkedHashMap<String, Object> lhm = new LinkedHashMap<String, Object>();
			lhm.put("keyname", exchange.getProperty("keyname"));
			lhm.put("value", m.get("data"));
			exchange.getIn().setBody(lhm);
		}
	}

}
