package edu.emmerson.camel.bill.camel_bill_create;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java8 DSL Router
 */
public class BillCreateRouteBuilder extends RouteBuilder {

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
	
		//http://127.0.0.1:8200
		from("direct:createbill")
			.log("appId=${sys.ENV_APP_ID} requestId=${headers.X-Request-ID} programme=${sys.ENV_PROGRAMME} env=${sys.ENV_TIER} tier=${sys.ENV_TIER} body=${body}")
			.toD("${properties:ENV_DISCOUNT_BACKEND_URL}/${headers.keyname}");
		
    }

}
