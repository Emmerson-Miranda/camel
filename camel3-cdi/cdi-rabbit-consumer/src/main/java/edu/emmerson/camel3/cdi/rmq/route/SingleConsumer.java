package edu.emmerson.camel3.cdi.rmq.route;

import org.apache.camel.builder.RouteBuilder;

public class SingleConsumer  extends RouteBuilder{

	private String queueName;

	public SingleConsumer(String queueName){
		this.queueName = queueName;
	}
	
	@Override
	public void configure() throws Exception {
		String exchangeName = "exchange" + queueName;
		String connectionString = "rabbitmq:" + exchangeName + "?connectionFactory=#consumerConnectionFactoryService&queue=" + queueName + "&routingKey=main&durable=true&autoDelete=false&automaticRecoveryEnabled=true&exclusive=false&autoAck=false&concurrentConsumers=2&prefetchCount=2&prefetchEnabled=true&transferException=true";

		from(connectionString)
			.routeId(queueName)
			.log("${body}");
	}

}
