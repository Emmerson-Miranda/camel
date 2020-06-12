package edu.emmerson.camel3.cdi.rmq;

import org.apache.commons.lang3.StringUtils;

public class ConfigReader {


	public static final String DISABLE_SUSPENSION = "DISABLE_SUSPENSION";

	public static final String RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_ENABLE = "RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_ENABLE";

	public static final String RABBIT_HOST = "RABBIT_HOST";

	public static final String RABBIT_PORT = "RABBIT_PORT";
	
	private static final String DEFAULT_UPSTREAM_CS = "undertow:http://upstream:10003/microservice/myservice?httpMethodRestrict=POST&exchangePattern=InOut";

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

		System.out.println("CONFIGURATION -> " + envVarName + "=" + tmp);
		return tmp;
	}
	
}
