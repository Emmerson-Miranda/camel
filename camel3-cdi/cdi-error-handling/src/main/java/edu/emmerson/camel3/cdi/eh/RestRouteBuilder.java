package edu.emmerson.camel3.cdi.eh;


import org.apache.camel.builder.RouteBuilder;

import edu.emmerson.camel3.cdi.eh.routes.InvalidNumberOnExceptionRoute;
import edu.emmerson.camel3.cdi.eh.routes.InvalidNumberRoute;
import edu.emmerson.camel3.cdi.eh.routes.InvalidNumberTryCatchRoute;
import edu.emmerson.camel3.cdi.eh.routes.NoErrorRoute;


public class RestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	
    	onException(Throwable.class)
		.log("onException:: ${header.X-Correlation-ID} :: ${exception.message} :: ${exception}")
		.setBody(constant("Error handling by " + RestRouteBuilder.class))
		;


        // configure we want to use undertow as the component for the rest DSL
        restConfiguration().component("undertow")
            .contextPath("/").host("0.0.0.0").port(8080)
            .apiContextPath("/api-doc")
            	.apiContextRouteId("api-doc-endpoint")
                .apiProperty("api.title", "Error Handling API").apiProperty("api.version", "1.0.0")
                // and enable CORS
                .apiProperty("cors", "true");

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
            .consumes("application/json").produces("application/json")

            .post("/noerror").id("noerror-resource").description("Happy path")
            .to(NoErrorRoute.DIRECT)

	        .post("/in").id("in-resource").description("Invalid number, without error handling")
	        .to(InvalidNumberRoute.DIRECT)
	        
	        .post("/inoe").id("inoe-resource").description("Invalid number, with on exception")
	        .to(InvalidNumberOnExceptionRoute.DIRECT)
	        
	        .post("/intc").id("intc-resource").description("Invalid number, with try catch")
	        .to(InvalidNumberTryCatchRoute.DIRECT)
	        
	        
	        
	        ;
       

    }

}
