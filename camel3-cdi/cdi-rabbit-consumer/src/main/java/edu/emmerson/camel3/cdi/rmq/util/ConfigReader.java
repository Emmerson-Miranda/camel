package edu.emmerson.camel3.cdi.rmq.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {

	private static final Logger logger  = LogManager.getLogger(ConfigReader.class);
	
	public static final String DISABLE_SUSPENSION = "DISABLE_SUSPENSION";

	public static final String RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_ENABLE = "RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_ENABLE";

	public static final String RABBIT_HOST = "RABBIT_HOST";

	public static final String RABBIT_PORT = "RABBIT_PORT";
	
	public static final String DEFAULT_UPSTREAM_CS = "http://localhost:10003/microservice/myservice"; //"http://upstream:10003/microservice/myservice"

	public static final String CAMEL_MAXIMUM_REDELIVERIES = "CAMEL_MAXIMUM_REDELIVERIES";

	public static final String CAMEL_REDELIVERY_DELAY_MS = "CAMEL_REDELIVERY_DELAY_MS";

	public static final String CAMEL_IDEMPOTENT_REPOSITORY_SIZE = "CAMEL_IDEMPOTENT_REPOSITORY_SIZE";

	public static String getUpstreamEndpoint() {
		return getEnvVar("RMQ_UPSTREAM_CS", DEFAULT_UPSTREAM_CS);
	}

	public static String getDLQEndpoint() {
		StringBuilder sbConsumer = new StringBuilder();
		sbConsumer.append("rabbitmq:myexchange?").append("connectionFactory=#consumerConnectionFactoryService")
				.append("&queue=myqueueDLQ").append("&durable=true").append("&autoDelete=false")
				.append("&automaticRecoveryEnabled=true").append("&exchangePattern=InOnly")
				.append("&routingKey=dlq").append("&exclusive=false").append("&autoAck=false")
				.append("&transferException=true");

		return getEnvVar("RMQ_CONSUMER_DLQ_CS", sbConsumer.toString());
	}

	public static String getQueueEndpoint() {
		StringBuilder sbConsumer = new StringBuilder();
		sbConsumer.append("rabbitmq:myexchange?").append("connectionFactory=#consumerConnectionFactoryService")
				.append("&queue=myqueue").append("&routingKey=main").append("&durable=true")
				.append("&autoDelete=false").append("&automaticRecoveryEnabled=true").append("&exclusive=false")
				.append("&autoAck=false").append("&concurrentConsumers=2").append("&prefetchCount=2")
				.append("&prefetchEnabled=true").append("&transferException=true");
	
		return getEnvVar("RMQ_CONSUMER_QUEUE_CS", sbConsumer.toString());
	}

	/**
	 * Throttle the number of messages to deliver to the upstream.
	 * @return
	 */
	public static long getDeliveryThrottle() {
		String pts = getEnvVar("DELIVERY_THROTTLE", "2"); //by default 2 messages
		return Long.parseLong(pts);
	}
	
	public static long getProcessTimeSimulationMs() {
		String pts = getEnvVar("PROCESS_TIME_SIMULATION_MS", "300");
		return Long.parseLong(pts);
	}
	
	public static boolean isDisableSuspensionEnabled() {
		return "true".equals( getEnvVar(DISABLE_SUSPENSION, "false") );
	}
	
	public static boolean isRabbitClientSleepOndisconnectionEnabled() {
		boolean disableSuspension = "true".equals( getEnvVar(RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_ENABLE, "false") );
		return disableSuspension;
	}
	
	public static int getRabbitClientSleepOndisconnectionMS() {
		return Integer.parseInt(getEnvVar("RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_MS", "5000"));
	}
	
	
	public static int getRabbitPort() {
		String tmp = getEnvVar(RABBIT_PORT, "5672");
		return Integer.parseInt(tmp);
	}
	
	public static String getRabbitVirtualHost() {
		return getEnvVar("RABBIT_VIRTUALHOST", "/");
	}
	
	public static String getRabbitPassword() {
		return getEnvVar("RABBIT_PWD", "guest");
	}
	
	public static String getRabbitUsername() {
		return getEnvVar("RABBIT_USER", "guest");
	}
	
	public static String getRabbitHost() {
		return getEnvVar(RABBIT_HOST, "rabbitmq");
	}
	
	public static String getEnvVar(String envVarName, String defaultValue) {
		String tmp = System.getenv(envVarName);
		if(StringUtils.isEmpty(tmp)) {
			tmp = System.getProperty(envVarName, defaultValue);
		}

		logger.info("CONFIGURATION -> " + envVarName + "=" + tmp);
		return tmp;
	}

	public static int getCamelMaximumRedeliveries() {
		String tmp = getEnvVar(CAMEL_MAXIMUM_REDELIVERIES, "2");
		return Integer.parseInt(tmp);
	}

	public static int getCamelRedeliveryDelay() {
		String tmp = getEnvVar(CAMEL_REDELIVERY_DELAY_MS, "1000");
		return Integer.parseInt(tmp);
	}
	
	public static int getCamelIdempotentRepositorySize() {
		String tmp = getEnvVar(CAMEL_IDEMPOTENT_REPOSITORY_SIZE, "200");
		return Integer.parseInt(tmp);
	}
	
}
