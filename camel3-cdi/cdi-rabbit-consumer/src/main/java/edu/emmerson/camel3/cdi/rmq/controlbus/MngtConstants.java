package edu.emmerson.camel3.cdi.rmq.controlbus;

public interface MngtConstants {
	
	String JMX_DOMAIN_NAME = "MyMetrics";
	
	String MNGT_CONSUMER_DIRECT_ENDPOINT = "direct:camel-mngt-controlbus-consumer";
	String MNGT_CONSUMER_DIRECT_ROUTE_ID = "mngt.controlbus.consumer.direct" ;
	String MNGT_CONSUMER_RABBITMQ_ROUTE_ID = "mngt.controlbus.consumer.amqp" ;

	String MNGT_PRODUCER_DIRECT_ENDPOINT = "direct:camel-mngt-controlbus-producer";
	String MNGT_PRODUCER_DIRECT_ROUTE_ID = "mngt.controlbus.producer.direct";
	String MNGT_PRODUCER_REST_ROUTE_ID   = "mngt.controlbus.producer.post";
	
	String MNGT_DESCRIBE_DIRECT_ENDPOINT = "direct:camel-mngt-controlbus-describe";
	String MNGT_DESCRIBE_DIRECT_ROUTE_ID = "mngt.controlbus.describe.direct";
	String MNGT_DESCRIBE_REST_ROUTE_ID   = "mngt.controlbus.describe.get";

	String MNGT_SHUTDOWN_DIRECT_ENDPOINT = "direct:camel-mngt-controlbus-shutdown";
	String MNGT_SHUTDOWN_DIRECT_ROUTE_ID = "mngt.controlbus.shutdown.direct";
	String MNGT_SHUTDOWN_REST_ROUTE_ID   = "mngt.controlbus.shutdown.get";
	
	String MNGT_SWAGGER_REST_ROUTE_ID   = "mngt.controlbus.swagger.get";
	
}
