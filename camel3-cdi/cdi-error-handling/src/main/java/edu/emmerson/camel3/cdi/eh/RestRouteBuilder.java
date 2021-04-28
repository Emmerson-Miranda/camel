package edu.emmerson.camel3.cdi.eh;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;

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
import edu.emmerson.camel3.cdi.eh.routes.YamlRoute;
import edu.emmerson.camel3.cdi.eh.routes.json.JsonValidationRoute;
import edu.emmerson.camel3.cdi.eh.routes.xml.XmlValidationRoute;


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

        MetricsRoutePolicy mrpNoErrorRoute = MetricsFactory.createMetricsRoutePolicy(NoErrorRoute.ROUTE_ID);
        MetricsRoutePolicy mrpNumberRoute = MetricsFactory.createMetricsRoutePolicy(NumberRoute.ROUTE_ID);
        MetricsRoutePolicy mrpInvalidNumberOnExceptionToRoute = MetricsFactory.createMetricsRoutePolicy(InvalidNumberOnExceptionToRoute.ROUTE_ID);
        MetricsRoutePolicy mrpInvalidNumberRouteOnExceptionSelfContainedRoute = MetricsFactory.createMetricsRoutePolicy(InvalidNumberRouteOnExceptionSelfContainedRoute.ROUTE_ID);
        MetricsRoutePolicy mrpInvalidNumberTryCatchRoute = MetricsFactory.createMetricsRoutePolicy(InvalidNumberTryCatchRoute.ROUTE_ID);
        MetricsRoutePolicy mrpErrorRoute = MetricsFactory.createMetricsRoutePolicy(ErrorRoute.ROUTE_ID);
        MetricsRoutePolicy mrpErrorOnExceptionRoute = MetricsFactory.createMetricsRoutePolicy(ErrorOnExceptionRoute.ROUTE_ID);
        MetricsRoutePolicy mrpErrorOnExceptionToRoute = MetricsFactory.createMetricsRoutePolicy(ErrorOnExceptionToRoute.ROUTE_ID);
        MetricsRoutePolicy mrpErrorOnExceptionToTryCatchRoute = MetricsFactory.createMetricsRoutePolicy(ErrorOnExceptionToTryCatchRoute.ROUTE_ID);
        MetricsRoutePolicy mrpCallHttpBackendUndertowRoute = MetricsFactory.createMetricsRoutePolicy(CallHttpBackendUndertowRoute.ROUTE_ID);
        MetricsRoutePolicy mrpCallHttpBackendHttpRoute = MetricsFactory.createMetricsRoutePolicy(CallHttpBackendHttpRoute.ROUTE_ID);
        MetricsRoutePolicy mrpXmlValidationRoute = MetricsFactory.createMetricsRoutePolicy(XmlValidationRoute.ROUTE_ID);
        MetricsRoutePolicy mrpJsonValidationRoute = MetricsFactory.createMetricsRoutePolicy(JsonValidationRoute.ROUTE_ID);

        
        rest("/eh").id("http-endpoint").description("Error Handling rest service")
            .consumes("application/json").produces("application/json")
            .post("/noerror").id("noerror-resource").description("Happy path")
            .route().routePolicy(mrpNoErrorRoute)
            .to(NoErrorRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        	.consumes("application/json").produces("application/json")
	        .post("/in").id("in-resource").description("Invalid number, without error handling")
	        .route().routePolicy(mrpNumberRoute)
	        .to(NumberRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/inoeto").id("inoe-resource").description("Invalid number, with on exception")
	        .route().routePolicy(mrpInvalidNumberOnExceptionToRoute)
	        .to(InvalidNumberOnExceptionToRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/inoesc").id("inoesc-resource").description("Invalid number Self Contained, with on exception")
	        .route().routePolicy(mrpInvalidNumberRouteOnExceptionSelfContainedRoute)
	        .to(InvalidNumberRouteOnExceptionSelfContainedRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/intc").id("intc-resource").description("Invalid number, with try catch")
	        .route().routePolicy(mrpInvalidNumberTryCatchRoute)
	        .to(InvalidNumberTryCatchRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/te").id("intc-resource").description("Throw a Exception without error handling")
	        .route().routePolicy(mrpErrorRoute)
	        .to(ErrorRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/teoe").id("intc-resource").description("Implementation Throw a Exception and is handled by onException")
	        .route().routePolicy(mrpErrorOnExceptionRoute)
	        .to(ErrorOnExceptionRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/teoet").id("intc-resource").description("Call a route that throw an exception and handle it with caller onException, in thi case onException is ignored.")
	        .route().routePolicy(mrpErrorOnExceptionToRoute)
	        .to(ErrorOnExceptionToRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/teoetc").id("intc-resource").description("Call a route that throw an exception and handle it with caller try/catch, in this case try/catch works.")
	        .route().routePolicy(mrpErrorOnExceptionToTryCatchRoute)
	        .to(ErrorOnExceptionToTryCatchRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/chbu").id("chbu-resource").description("Call backend HTTP using Undertow component")
	        .route().routePolicy(mrpCallHttpBackendUndertowRoute)
	        .to(CallHttpBackendUndertowRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/chbh").id("chbh-resource").description("Call backend HTTP using HTTP component")
	        .route().routePolicy(mrpCallHttpBackendHttpRoute)
	        .to(CallHttpBackendHttpRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/schema/xml").id("sxc-resource").description("Call XML schema validation")
	        .route().routePolicy(mrpXmlValidationRoute)
	        .to(XmlValidationRoute.DIRECT);

        rest("/eh").id("http-endpoint").description("Error Handling rest service")
        .consumes("application/json").produces("application/json")
	        .post("/schema/json").id("sjc-resource").description("Call JSON schema validation")
	        .route().routePolicy(mrpJsonValidationRoute)
	        .to(JsonValidationRoute.DIRECT)
	        ;
       
        rest("/yaml").id("http-yaml-endpoint").description("Yaml endpoint")
	        .get("/simple").id("yaml-simple-resource").description("Simple")
	        .to(YamlRoute.DIRECT)
	        ;

    }

}
