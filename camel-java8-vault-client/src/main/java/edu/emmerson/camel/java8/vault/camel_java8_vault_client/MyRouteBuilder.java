package edu.emmerson.camel.java8.vault.camel_java8_vault_client;

import static edu.emmerson.camel.java8.vault.camel_java8_vault_client.Constants.QUERYPARAM_KEYNAME;
import static edu.emmerson.camel.java8.vault.camel_java8_vault_client.Constants.X_VAULT_TOKEN;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;


public class MyRouteBuilder extends RouteBuilder {

	public void configure() {

		onException(Exception.class)
			.handled(true)
			.bean(ExceptionProcessor.class)
			.marshal().json(JsonLibrary.Jackson)
		.end();

		restConfiguration().component("undertow")
			.contextPath("proxy").apiContextPath("/api-doc")
			.host("0.0.0.0").port(8080);

		rest("/resolve")
			.get("/key")
				.param().name(QUERYPARAM_KEYNAME)
					.type(RestParamType.query).defaultValue("false").description("Key to resolve")
				.endParam()
				.produces("application/json")
				.description("Resolve consul/vault keys")
				.outType(java.util.LinkedHashMap.class)
				.to("direct:vault");

		from("direct:vault")
			.setHeader(X_VAULT_TOKEN, simple("${properties:PARAM_VAULT_TOKEN}"))
			.setProperty(QUERYPARAM_KEYNAME, simple("${headers.keyname}"))
			.toD("undertow:${properties:PARAM_VAULT_URL}/${headers.keyname}")
				.unmarshal().json(JsonLibrary.Jackson)
				.bean(HashicorpVaultProcessor.class)
				.marshal().json(JsonLibrary.Jackson);
	}

}
