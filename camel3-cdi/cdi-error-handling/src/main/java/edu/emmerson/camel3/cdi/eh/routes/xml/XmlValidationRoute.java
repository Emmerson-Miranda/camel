package edu.emmerson.camel3.cdi.eh.routes.xml;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @see https://camel.apache.org/components/latest/validator-component.html
 * @author emmersonmiranda
 *
 */
public class XmlValidationRoute extends RouteBuilder {

	public static final String DIRECT = "direct:" + XmlValidationRoute.class.getSimpleName();

	public static final String ROUTE_ID = XmlValidationRoute.class.getSimpleName();

	@Override
	public void configure() throws Exception {

		onException(org.apache.camel.support.processor.validation.SchemaValidationException.class)
			.handled(true)
			.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
			.transform()
				.constant("Validation Error handling by " + ROUTE_ID);
		;

		onException(org.apache.camel.FailedToCreateProducerException.class)
			.handled(true)
			.log("Route onException :: ${exchangeId} :: ${routeId} :: ${exception.message} ")
			.transform()
				.constant("FailedToCreateProducerException Error handling by " + ROUTE_ID);
		;
		
		
		File f = new File(".");
		String validatorEndpoint = "validator:file:" + f.getAbsolutePath() + "/src/main/resources/schemas/${header.x-schema}";
		

		from(DIRECT)
			.routeId(ROUTE_ID)
			.log("Route Start :: ${exchangeId} :: ${routeId}")
			//.toD("validator:schemas/${header.x-schema}")
			.toD(validatorEndpoint)
			.log("Body :: ${body}")
			.log("Route End :: ${exchangeId} :: ${routeId}");

	}

}
