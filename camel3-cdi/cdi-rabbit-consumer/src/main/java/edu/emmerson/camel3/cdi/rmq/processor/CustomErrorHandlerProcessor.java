package edu.emmerson.camel3.cdi.rmq.processor;

import java.nio.channels.ClosedChannelException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.emmerson.camel3.cdi.rmq.util.ConfigReader;

public class CustomErrorHandlerProcessor implements Processor {
	
	private static final Logger logger  = LogManager.getLogger("onException");

	private final String methodName;
	
	public CustomErrorHandlerProcessor(String mn) {
		this.methodName = mn;
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		printExchange(this.methodName, exchange);
		analyzeException(this.methodName, exchange);
	}
	
	private void analyzeException(String prefix, Exchange exchange) throws InterruptedException {
		System.out.println(prefix + ":analyzing exception");
		Exception e = (Exception) exchange.getProperties().get(Exchange.EXCEPTION_CAUGHT);
		if(
				(e instanceof ClosedChannelException)
				|| (e instanceof com.rabbitmq.client.TopologyRecoveryException)
				|| (e instanceof com.rabbitmq.client.AlreadyClosedException)
				|| (e instanceof com.rabbitmq.client.ShutdownSignalException)
				|| (e instanceof java.net.ConnectException)
				
		) {
			if(ConfigReader.isRabbitClientSleepOndisconnectionEnabled()) {
				int disconnectionIdle = ConfigReader.getRabbitClientSleepOndisconnectionMS();
				System.out.println(prefix + ": Thread.sleep ms: " + disconnectionIdle);
				Thread.sleep(disconnectionIdle);
			}else {
				System.out.println(prefix + ": Thread.sleep disabled.");
			}
			
		}
		
		/*
		HttpOperationFailedException ex  = (HttpOperationFailedException) exchange.getProperties().get("CamelExceptionCaught");
		System.out.println(ex.getStatusCode());
		System.out.println(ex.getStatusText());
		System.out.println(ex.getMessage());
		*/
	}
	
	private void printExchange(String prefix, Exchange exchange) {
		StringBuilder sb = new StringBuilder();

		sb.append("\n........................................................\n");
		exchange.getProperties().forEach((k, v) -> {
			sb.append(prefix).append(":property:" + k + ":" + v).append("\n");
		});
		exchange.getIn().getHeaders().forEach((k, v) -> {
			sb.append(prefix).append(":header:" + k + ":" + v).append("\n");
		});
		String body =  exchange.getIn().getBody().getClass().getName();
		sb.append(prefix).append(":body:" + body ).append("\n");
		sb.append("........................................................\n");

		logger.debug(sb.toString());
	    	
		System.out.println(sb.toString());
	}

	
}
