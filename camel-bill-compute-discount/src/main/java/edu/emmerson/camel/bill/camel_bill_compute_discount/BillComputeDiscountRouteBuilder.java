package edu.emmerson.camel.bill.camel_bill_compute_discount;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * A Camel Java8 DSL Router
 */
public class BillComputeDiscountRouteBuilder extends RouteBuilder {

    public void configure() {
		restConfiguration().component("undertow")
		.contextPath("proxy").apiContextPath("/api-doc")
		.host("0.0.0.0")
		.port(8090);

		rest("/compute")
			.get("/prove/liveness")
				.to("direct:liveness")
			.get("/prove/readiness")
				.to("direct:readiness")
			.post("/discount")
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
			.bean(ReadinessProcessor.class);

		from("direct:createbill")
			.log("appId=${sys.ENV_APP_ID} hostname=${sys.HOSTNAME} requestId=${headers.X-Request-ID} programme=${sys.ENV_PROGRAMME} env=${sys.ENV_NAME} tier=${sys.ENV_TIER} body=${body}")
			.unmarshal().json(JsonLibrary.Jackson)
			.bean(BillComputeDiscountProcessor.class)
			.marshal().json(JsonLibrary.Jackson);
		
    }

}
