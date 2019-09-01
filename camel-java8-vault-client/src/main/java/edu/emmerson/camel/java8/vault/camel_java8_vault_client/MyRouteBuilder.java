package edu.emmerson.camel.java8.vault.camel_java8_vault_client;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;

import com.fasterxml.jackson.core.JsonParseException;

import static edu.emmerson.camel.java8.vault.camel_java8_vault_client.Constants.*;


/**
 * A Camel Java8 DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {


    public void configure() {

    	restConfiguration()
	        .component("undertow")
	        .contextPath("proxy")
	        .apiContextPath("/api-doc")
	        .host("0.0.0.0")
	        .port(8080);
	
	    rest("/resolve")
	        .get("/key")
	        .param().name(QUERYPARAM_KEYNAME).type(RestParamType.query).defaultValue("false").description("Key name to resolve").endParam()
	        .produces("application/json").description("Resolve consul/vault keys").outType(java.util.LinkedHashMap.class)
	        .to("direct:vault");
      
    	from("direct:vault")
    		.setHeader(X_VAULT_TOKEN, simple("${properties:PARAM_VAULT_TOKEN}"))
    	  	.setProperty(QUERYPARAM_KEYNAME, simple("${headers.keyname}"))
    	  .toD("undertow:${properties:PARAM_VAULT_URL}/${headers.keyname}")
    	  	//.log("Vault response body: ${body}")
    	  	.unmarshal().json(JsonLibrary.Jackson)
    	  	.bean(HashicorpVaultProcessor.class)
    	  	.marshal().json(JsonLibrary.Jackson)
    	  .onException(HttpOperationFailedException.class)
    	    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
    	    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
    	    .setBody().constant("{message='Failure getting key from vault server'}")
    	  .onException(JsonParseException.class)
    	    .handled(true)
    	    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
    	    .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
    	    .setBody().constant("Invalid json data");
    }


}
