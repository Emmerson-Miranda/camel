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
			.get("/prove/liveness")
				.to("direct:liveness")
			.get("/prove/readiness")
				.to("direct:readiness")		
			.post("/bill")
				.produces("application/json")
				.consumes("application/json")
				.description("Demo bill")
				.outType(Bill.class)
				.to("direct:createbill");
		
		from("direct:liveness")
			.log("appId=${sys.ENV_APP_ID} hostname=${sys.HOSTNAME} requestId=${headers.X-Request-ID} programme=${sys.ENV_PROGRAMME} env=${sys.ENV_NAME} tier=${sys.ENV_TIER} body=Liveness")
			.bean(LivenessProcessor.class);
		
		from("direct:readiness")
			.log("appId=${sys.ENV_APP_ID} hostname=${sys.HOSTNAME} requestId=${headers.X-Request-ID} programme=${sys.ENV_PROGRAMME} env=${sys.ENV_NAME} tier=${sys.ENV_TIER} body=Readiness")
			.toD("${properties:ENV_DISCOUNT_BACKEND_URL}/prove/liveness");

		from("direct:createbill")
			.log("appId=${sys.ENV_APP_ID} hostname=${sys.HOSTNAME} requestId=${headers.X-Request-ID} programme=${sys.ENV_PROGRAMME} env=${sys.ENV_NAME} tier=${sys.ENV_TIER} body=${body}")
			.toD("${properties:ENV_DISCOUNT_BACKEND_URL}/discount");
		
    }

}
