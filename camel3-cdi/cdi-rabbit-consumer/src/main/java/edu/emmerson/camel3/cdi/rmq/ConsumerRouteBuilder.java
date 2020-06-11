package edu.emmerson.camel3.cdi.rmq;

import java.nio.channels.ClosedChannelException;
import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/exception-clause.html
 *
 */
public class ConsumerRouteBuilder extends RouteBuilder {

	public static final String RABBITMQ_ROUTING_KEY = "rabbit.consumer";

	@Override
	public void configure() throws Exception {

		boolean disableSuspension = ConfigReader.isDisableSuspensionEnabled();

		long lpts = ConfigReader.getProcessTimeSimulationMs();

		//
		// error handling
		//
		onException(Throwable.class)
			.maximumRedeliveries(2)
			.handled(true)
			.log("onException:: ${header.X-Correlation-ID} :: ${exception.message} :: ${exception}")
			.asyncDelayedRedelivery()
			.redeliveryDelay(1000)
			.onExceptionOccurred(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					printExchange("onExceptionOccurred", exchange);
					analyzeException("onExceptionOccurred", exchange);
				}
			})
			.onRedelivery(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					printExchange("onRedelivery", exchange);
					analyzeException("onExceptionOccurred", exchange);
				}
	
			})
			// .log("\"Error reported: ${exception.message} - cannot process this message.\"
			// - retry ${headers.rabbitmq.DELIVERY_TAG}")
			.process(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					printExchange("onException", exchange);
					analyzeException("onExceptionOccurred", exchange);
				}
				
			})
			.setHeader(RabbitMQConstants.ROUTING_KEY, constant("dlq"))
			// sending the message to DLQ
			.toD(ConfigReader.getDLQEndpoint())
			.choice()
				.when(constant(disableSuspension).isEqualTo(false))
					.log("Stop consumers enabled - stopping them")
					.process().message(m -> {
						int restartDelayInMilis = 30000;
						LinkedHashMap<String, String> body = MngtProducerRouteBuilder.buildMngtMessage(
								ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID, true, RABBITMQ_ROUTING_KEY,
								restartDelayInMilis);
						m.setBody(body);
					})
					.to(MngtConstants.MNGT_PRODUCER_DIRECT_ENDPOINT).otherwise()
					.log("Stop consumers disabled by DISABLE_SUSPENSION environment variable.")
			.end();

		MetricsRoutePolicy mrp = MetricsFactory.createMetricsRoutePolicy(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID);

		//
		// consuming messages
		//
		from(ConfigReader.getQueueEndpoint())
			.routeId(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID)
			.routePolicy(mrp)
			.to("direct:target");

		//
		// processing message
		//
		from("direct:target")
			.routeId(ConsumerConstants.CONSUMER_DIRECT_ROUTE_ID)
			.log("Message to send: ${header.X-Correlation-ID}")
			.choice()
				.when(header("test-scenario").isEqualTo("ko")).process((m) -> {
					throw new Exception("Some error happen!");
				})
			.end()
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println("Processing message!");
					Thread.sleep(lpts);
				}
			})
			.to(ConfigReader.getUpstreamEndpoint())
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println("Message delivered!");
				}
			})
			.log("Message sended: ${header.X-Correlation-ID}");
		;
	}

	private void analyzeException(String prefix, Exchange exchange) throws InterruptedException {
		System.out.println(prefix + ":analyzing exception");
		Exception e = (Exception) exchange.getProperties().get(Exchange.EXCEPTION_CAUGHT);
		if(
				(e instanceof ClosedChannelException)
				|| (e instanceof com.rabbitmq.client.TopologyRecoveryException)
				|| (e instanceof com.rabbitmq.client.AlreadyClosedException)
				|| (e instanceof com.rabbitmq.client.ShutdownSignalException)
		) {
			int disconnectionIdle = ConfigReader.getDisconnectionIdle();
			System.out.println(prefix + ": Thread.sleep " + disconnectionIdle);
			Thread.sleep(disconnectionIdle);
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
		sb.append(prefix).append(":body:" + exchange.getIn().getBody().toString()).append("\n");
		sb.append("........................................................\n");

		System.out.println(sb.toString());
	}

}
