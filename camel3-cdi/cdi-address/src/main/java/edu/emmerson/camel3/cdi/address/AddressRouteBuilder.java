package edu.emmerson.camel3.cdi.address;


import org.apache.camel.BeanInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;


public class AddressRouteBuilder extends RouteBuilder {
	
	@BeanInject("addressService")
	private AddressService addressService;

    @Override
    public void configure() throws Exception {

        // configure we want to use undertow as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("undertow")
            // use json binding mode so Camel automatic binds json <--> pojo
            .bindingMode(RestBindingMode.json)
            // and output using pretty print
            .dataFormatProperty("prettyPrint", "true")
            // setup context path on localhost and port number that netty will use
            .contextPath("/").host("0.0.0.0").port(8080)
            // add swagger api-doc out of the box
            .apiContextPath("/api-doc")
                .apiProperty("api.title", "Address API").apiProperty("api.version", "1.0.0")
                // and enable CORS
                .apiProperty("cors", "true");

        // this user REST service is json only
        rest("/address").description("Address rest service")
            .consumes("application/json").produces("application/json")

            .get("/findAll").description("Find all addresses")
                .responseMessage().code(200).message("All Addresses").endResponseMessage()
                .to("direct:myAddressService");
        
        from("direct:myAddressService").bean(addressService, "listAddresses");
    }

}
