package edu.emmerson.camel.bill.camel_bill_create;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java8 DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {

		restConfiguration().component("undertow")
		.contextPath("proxy").apiContextPath("/api-doc")
		.host("0.0.0.0")
		.port(8080);

		rest("/demo")
			.post("/bill")
				.produces("application/json")
				.consumes("application/json")
				.description("Demo bill")
				.outType(Bill.class)
				.to("direct:createbill");
	
		from("direct:createbill")
			.log("requestId=${headers.X-Request-ID} programme=${sys.ENV_PROGRAMME} env=${sys.ENV_TIER} tier=${sys.ENV_TIER} body=${body}")
			.setBody().simple("${body}");
		
    }

}
