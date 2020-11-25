package edu.emmerson.camel3.cdi.rmq.util;

public interface ConsumerConstants {
	
	String CONSUMER_RABBITMQ_ROUTE_ID = RouteIdGenerator.newRouteId("mymscode", "myqueueName", RouteIdGenerator.RouteType.AMQP_CONSUMER);
	String CONSUMER_DIRECT_ROUTE_ID = RouteIdGenerator.newRouteId("mymscode", "myqueueName", RouteIdGenerator.RouteType.CAMEL_DIRECT);
	
	
	String STATS_DIRECT_ROUTE_ID = RouteIdGenerator.newRouteId("mymscode", "stats", RouteIdGenerator.RouteType.CAMEL_DIRECT);
	String STATS_DIRECT_REST_ROUTE_ID = RouteIdGenerator.newRouteId("mymscode", "stats", RouteIdGenerator.RouteType.HTTP_GET);
	
}
