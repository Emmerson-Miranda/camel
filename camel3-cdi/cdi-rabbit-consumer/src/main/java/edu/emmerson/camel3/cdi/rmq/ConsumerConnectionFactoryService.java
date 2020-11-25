package edu.emmerson.camel3.cdi.rmq;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import edu.emmerson.camel3.cdi.rmq.util.ConfigReader;

/**
 * docker run -d --hostname rabbitmqserver --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
 * 
 * @author emmersonmiranda
 *
 */
@ApplicationScoped
@Named("consumerConnectionFactoryService")
public class ConsumerConnectionFactoryService extends com.rabbitmq.client.ConnectionFactory {

	public ConsumerConnectionFactoryService() {
		super();
		super.setHost(ConfigReader.getRabbitHost());
		super.setPort(ConfigReader.getRabbitPort());
		super.setUsername(ConfigReader.getRabbitUsername());
		super.setPassword(ConfigReader.getRabbitPassword());
		super.setVirtualHost(ConfigReader.getRabbitVirtualHost());
	}
	

}
