package edu.emmerson.camel3.cdi.eh;


import org.apache.camel.builder.RouteBuilder;

import edu.emmerson.camel3.cdi.eh.routes.CallHttpBackendHttpRoute;
import edu.emmerson.camel3.cdi.eh.routes.CallHttpBackendUndertowRoute;
import edu.emmerson.camel3.cdi.eh.routes.ErrorOnExceptionRoute;
import edu.emmerson.camel3.cdi.eh.routes.ErrorOnExceptionToRoute;
import edu.emmerson.camel3.cdi.eh.routes.ErrorOnExceptionToTryCatchRoute;
import edu.emmerson.camel3.cdi.eh.routes.ErrorRoute;
import edu.emmerson.camel3.cdi.eh.routes.InvalidNumberOnExceptionToRoute;
import edu.emmerson.camel3.cdi.eh.routes.InvalidNumberRouteOnExceptionSelfContainedRoute;
import edu.emmerson.camel3.cdi.eh.routes.InvalidNumberTryCatchRoute;
import edu.emmerson.camel3.cdi.eh.routes.NoErrorRoute;
import edu.emmerson.camel3.cdi.eh.routes.NumberRoute;
import edu.emmerson.camel3.cdi.eh.routes.xml.ValidationRoute;


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
	        .to(NumberRoute.DIRECT)
	        
	        .post("/inoeto").id("inoe-resource").description("Invalid number, with on exception")
	        .to(InvalidNumberOnExceptionToRoute.DIRECT)
	        
	        .post("/inoesc").id("inoesc-resource").description("Invalid number Self Contained, with on exception")
	        .to(InvalidNumberRouteOnExceptionSelfContainedRoute.DIRECT)
	        
	        .post("/intc").id("intc-resource").description("Invalid number, with try catch")
	        .to(InvalidNumberTryCatchRoute.DIRECT)
	        
	        .post("/te").id("intc-resource").description("Throw a Exception without error handling")
	        .to(ErrorRoute.DIRECT)
	        
	        .post("/teoe").id("intc-resource").description("Implementation Throw a Exception and is handled by onException")
	        .to(ErrorOnExceptionRoute.DIRECT)
	        
	        .post("/teoet").id("intc-resource").description("Call a route that throw an exception and handle it with caller onException, in thi case onException is ignored.")
	        .to(ErrorOnExceptionToRoute.DIRECT)
	        
	        .post("/teoetc").id("intc-resource").description("Call a route that throw an exception and handle it with caller try/catch, in this case try/catch works.")
	        .to(ErrorOnExceptionToTryCatchRoute.DIRECT)
	        
	        .post("/chbu").id("chbu-resource").description("Call backend HTTP using Undertow component")
	        .to(CallHttpBackendUndertowRoute.DIRECT)
	        
	        .post("/chbh").id("chbh-resource").description("Call backend HTTP using HTTP component")
	        .to(CallHttpBackendHttpRoute.DIRECT)
	        
	        .post("/schema/xml").id("sxc-resource").description("Call schema validation")
	        .to(ValidationRoute.DIRECT)
	        
	        
	        ;
       

    }

}
