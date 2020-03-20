package edu.emmerson.camel3.cdi.rmq;


import javax.inject.Named;

/**
 * docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
 * 
 * @author emmersonmiranda
 *
 */
@Named("consumerConnectionFactoryService")
public class ConsumerConnectionFactoryService extends com.rabbitmq.client.ConnectionFactory {

	public ConsumerConnectionFactoryService() {
		super();
		super.setHost("localhost");
		super.setPort(5672);
		super.setUsername("guest");
		super.setPassword("guest");
		super.setVirtualHost("/");
		super.setAutomaticRecoveryEnabled(true);
	}

}
