package edu.emmerson.camel3.cdi.rmq;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;


public class MngtRestRouteBuilder extends RouteBuilder {

	@Override
    public void configure() throws Exception {

        // configure we want to use undertow as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("undertow")
            // use json binding mode so Camel automatic binds json <--> pojo
            .bindingMode(RestBindingMode.json)
            .contextPath("/").host("0.0.0.0").port(9090)
            .apiContextPath("/api-doc")
            	.apiContextRouteId("api-doc-endpoint")
                .apiProperty("api.title", "Producer API").apiProperty("api.version", "1.0.0")
                .apiProperty("cors", "true");

        // this user REST service is json only
        rest("/mngt").id("mngt-endpoint").description("RabbitMQ Camel Management service")
            .consumes("application/json").produces("application/json")

            .post("/publish").id("mngt-endpoint-publish").description("Publish a Mngt message in RabbitMQ")
                .responseMessage().code(204).message("Message storaged").endResponseMessage()
                .to(MngtProducerRouteBuilder.DIRECT_PUBLISH_MESSAGE_MNGT_PRODUCER_ENDPOINT)
                
            .get("/shutdown").id("mngt-endpoint-shutdown").description("Shutdown Apache Camel")
                .to(ShutdownRouteBuilder.DIRECT_SHUTDOWN_MESSAGE_CONSUMERS_ENDPOINT)
        ;
        
    }
    
}
