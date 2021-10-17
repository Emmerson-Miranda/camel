package edu.emmerson.camel3.cdi.rmq.route;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class MultipleConsumers  extends RouteBuilder{

	@Inject
	CamelContext context;
	
	@Override
	public void configure() throws Exception {
		System.out.println("Instantiating multiple consumers.");
		context.addRoutes(new SingleConsumer("qa"));
		context.addRoutes(new SingleConsumer("qb"));
		context.addRoutes(new SingleConsumer("qc"));
		context.addRoutes(new SingleConsumer("qd"));
		
        // this user REST service is json only
        rest("/consumer").id("rmq-endpoint").description("RabbitMQ rest service")
        	.consumes("application/json").produces("application/json")
            .put("/create")
                .to("direct:createConsumer");

        from("direct:createConsumer")
        .process().message(m -> {
        	LinkedHashMap req = m.getBody(LinkedHashMap.class);
        	try {
        		SingleConsumer sc = new SingleConsumer((String)req.get("queueName"));
        		// curl -d '{"queueName": "qf"}' -X put http://0.0.0.0:9090/consumer/create
				m.getExchange().getContext().addRoutes(sc);
				System.out.println("route registred");
				req.put("created", "true");
				m.setBody(req);
			} catch (Exception e) {
				e.printStackTrace();
			}
        })
       // .marshal().json(JsonLibrary.Jackson)
        ;
        
	}

}
