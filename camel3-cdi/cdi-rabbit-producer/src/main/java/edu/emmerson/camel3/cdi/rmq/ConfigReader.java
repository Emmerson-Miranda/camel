package edu.emmerson.camel3.cdi.rmq;

import org.apache.commons.lang3.StringUtils;

public class ConfigReader {


	public static final String RABBIT_HOST = "RABBIT_HOST";
	public static final String RABBIT_PORT = "RABBIT_PORT";
	

	public static String getQueueEndpoint() {
		StringBuilder sbPub = new StringBuilder();
        sbPub.append("rabbitmq:myexchange?")
        .append("connectionFactory=#producerConnectionFactoryService")
        .append("&routingKey=main")
        .append("&queue=myqueue")
        .append("&durable=true")
        .append("&autoDelete=false")
        .append("&exclusive=false")
        .append("&exchangePattern=InOnly")
        ;
		return getEnvVar("RMQ_PRODUCER_QUEUE_CS", sbPub.toString());
	}

	public static long getProcessTimeSimulationMs() {
		String pts = getEnvVar("PROCESS_TIME_SIMULATION_MS", "100");
		return Long.parseLong(pts);
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
