package edu.emmerson.camel3.cdi.rmq;

public interface ConsumerConstants {
	
	String JMX_DOMAIN_NAME = "MyMetrics";
	
	String CONSUMER_ROUTE_ID = "myms.rmq.consumer.myqueue.routeid";
	String STATUS_ROUTE_ID = "myms.rest.get.status.routeid";
	
	String MNGT_CONSUMER_ROUTE_ID = "mngt.rmq.consumer.controlbus.routeid";
	String MNGT_PRODUCER_ROUTE_ID = "mngt.rest.post.producer.routeid";

	
}
