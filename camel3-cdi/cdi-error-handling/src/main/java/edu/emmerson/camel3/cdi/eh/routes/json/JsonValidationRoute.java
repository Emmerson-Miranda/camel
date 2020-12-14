package edu.emmerson.camel3.cdi.eh.routes.json;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @see https://camel.apache.org/components/latest/validator-component.html
 * @author emmersonmiranda
 *
 */
public class JsonValidationRoute extends RouteBuilder {

	public static final String DIRECT = "direct:" + JsonValidationRoute.class.getSimpleName();

	public static final String ROUTE_ID = JsonValidationRoute.class.getSimpleName();

	@Override
	public void configure() throws Exception {

		onException(com.networknt.schema.JsonSchemaException.class)
		.handled(true)
		.log("Route JsonSchemaException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
		.transform()
			.constant("Invalid schema definition handling by " + ROUTE_ID);
		;
	
		onException(org.apache.camel.component.jsonvalidator.JsonValidationException.class)
			.handled(true)
			.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
			.transform()
				.constant("Validation Error handling by " + ROUTE_ID);
		;

		onException(java.io.FileNotFoundException.class)
			.handled(true)
			.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
			.transform()
				.constant("FileNotFoundException Error handling by " + ROUTE_ID);
		;
		
		File f = new File(".");
		String validatorEndpoint = "json-validator:file:" + f.getAbsolutePath() + "/src/main/resources/schemas/${header.x-schema}";
		

		from(DIRECT)
			.routeId(ROUTE_ID)
			.log("Route Start :: ${exchangeId} :: ${routeId}")
			//.toD("json-validator:schemas/${header.x-schema}")
			.toD(validatorEndpoint)
			.log("Body :: ${body}")
			.log("Route End :: ${exchangeId} :: ${routeId}");

	}

}
