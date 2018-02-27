package edu.emmerson.camel.springboot.helloworld;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;


@Component
public class SayHelloRouter extends RouteBuilder {

	@Override
    public void configure() {
		
		restConfiguration().contextPath("/camel").bindingMode(RestBindingMode.json);
		
        rest("/say/")
            .produces("application/json")
            .get("hello")
            	.produces("text/plain")
                .route()
                .transform().constant("Hello World!")
                .log("${body}")
                .endRest()
            .get("hello/{name}")
            	.produces("text/plain")
                .route()
                .bean("hello")
                .log("${body}")
                .endRest()
            .get("helloObject/{name}")
                .route()
                .to("bean:hello?method=sayHelloObject")
                .log("${body}")
                .endRest()
            .get("greetings/{name}")
            	.produces("text/plain")
                .route()
                .to("bean:hello?method=greetings")
                .log("${body}")
                .endRest()
               ;
	}

}
