package edu.emmerson.camel3.cdi.rmq.controlbus;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.model.rest.RestBindingMode;

import edu.emmerson.camel3.cdi.rmq.route.StatsRouteBuilder;
import edu.emmerson.camel3.cdi.rmq.util.ConsumerConstants;
import edu.emmerson.camel3.cdi.rmq.util.MetricsFactory;


/**
 * https://issues.apache.org/jira/browse/CAMEL-14944
 * 
 * @author emmersonmiranda
 *
 */
public class MngtRestRouteBuilder extends RouteBuilder {

	@Override
    public void configure() throws Exception {
		
        restConfiguration().component("undertow")
            .bindingMode(RestBindingMode.json)
            .contextPath("/").host("0.0.0.0").port(9090)
            .apiContextPath("/api-doc")
            	.apiContextRouteId(MngtConstants.MNGT_SWAGGER_REST_ROUTE_ID)
                .apiProperty("api.title", "Producer API").apiProperty("api.version", "1.0.0")
                .apiProperty("cors", "true");
		
        MetricsRoutePolicy mrpPublish = MetricsFactory.createMetricsRoutePolicy(MngtConstants.MNGT_PRODUCER_REST_ROUTE_ID);	
        rest("/mngt").description("RabbitMQ Camel Management service")
	    	.consumes("application/json").produces("application/json")
	        .post("/publish")
	        	.description("Publish a Mngt message in RabbitMQ")
	        	.responseMessage().code(204).message("Message storaged").endResponseMessage()
	        	.route().routeId(MngtConstants.MNGT_PRODUCER_REST_ROUTE_ID).routePolicy(mrpPublish)
	        	.to(MngtConstants.MNGT_PRODUCER_DIRECT_ENDPOINT)
        ;
        
        MetricsRoutePolicy mrpDescribe = MetricsFactory.createMetricsRoutePolicy(MngtConstants.MNGT_DESCRIBE_REST_ROUTE_ID);	
        rest("/mngt").description("Describe Camel Routes inside the JVM")
	        .consumes("application/json").produces("application/json") 
	        .get("/describe")
		        .description("Describe Apache Camel routes").route().routeId(MngtConstants.MNGT_DESCRIBE_REST_ROUTE_ID).routePolicy(mrpDescribe)
		        .to(MngtConstants.MNGT_DESCRIBE_DIRECT_ENDPOINT)
        ;
        
       rest("/mngt").description("RabbitMQ Camel Management service")
	        .consumes("application/json").produces("application/json")
	        .get("/shutdown")
		        .description("Shutdown Apache Camel").route().routeId(MngtConstants.MNGT_SHUTDOWN_REST_ROUTE_ID)
		        .to(MngtConstants.MNGT_SHUTDOWN_DIRECT_ENDPOINT)
        ;  
 
       MetricsRoutePolicy mrpStats = MetricsFactory.createMetricsRoutePolicy(ConsumerConstants.STATS_DIRECT_REST_ROUTE_ID);	
       rest("/mngt").description("RabbitMQ Camel Management service")
	        .consumes("application/json").produces("application/json") 
	        .get("/stats")
		        .description("Stats Apache Camel").route().routeId(ConsumerConstants.STATS_DIRECT_REST_ROUTE_ID).routePolicy(mrpStats)
		        .to(StatsRouteBuilder.DIRECT_STATS)
       ;
       
    }
    
}
