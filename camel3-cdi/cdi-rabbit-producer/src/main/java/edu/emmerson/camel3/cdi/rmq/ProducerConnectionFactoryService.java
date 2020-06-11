package edu.emmerson.camel3.cdi.rmq;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * docker run -d --hostname rabbitmqserver --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
 * 
 * @author emmersonmiranda
 *
 */
@ApplicationScoped
@Named("producerConnectionFactoryService")
public class ProducerConnectionFactoryService extends com.rabbitmq.client.ConnectionFactory {

	public ProducerConnectionFactoryService() {
		super();
		super.setHost(ConfigReader.getRabbitHost());
		super.setPort(ConfigReader.getRabbitPort());
		super.setUsername(ConfigReader.getRabbitUsername());
		super.setPassword(ConfigReader.getRabbitPassword());
		super.setVirtualHost(ConfigReader.getRabbitVirtualHost());
	}

}
