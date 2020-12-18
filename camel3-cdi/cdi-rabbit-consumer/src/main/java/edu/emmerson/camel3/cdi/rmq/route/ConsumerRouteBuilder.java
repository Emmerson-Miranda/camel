package edu.emmerson.camel3.cdi.rmq.route;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

import edu.emmerson.camel3.cdi.rmq.MyIdempotentRepository;
import edu.emmerson.camel3.cdi.rmq.controlbus.MngtConstants;
import edu.emmerson.camel3.cdi.rmq.controlbus.MngtProducerRouteBuilder;
import edu.emmerson.camel3.cdi.rmq.processor.AfterSendToBackendProcessor;
import edu.emmerson.camel3.cdi.rmq.processor.BeforeSendToBackendProcessor;
import edu.emmerson.camel3.cdi.rmq.processor.CustomErrorHandlerProcessor;
import edu.emmerson.camel3.cdi.rmq.util.ConfigReader;
import edu.emmerson.camel3.cdi.rmq.util.ConsumerConstants;
import edu.emmerson.camel3.cdi.rmq.util.MetricsFactory;

import static org.apache.camel.component.http.HttpMethods.POST;
import static org.apache.camel.Exchange.HTTP_METHOD;
import static org.apache.camel.Exchange.HTTP_URI;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/exception-clause.html
 *
 */
public class ConsumerRouteBuilder extends RouteBuilder {

	@Inject
	MyIdempotentRepository myIdempotentRepository;
	
	public static final String RABBITMQ_ROUTING_KEY = "rabbit.consumer";

	@Override
	public void configure() throws Exception {

		boolean disableSuspension = ConfigReader.isDisableSuspensionEnabled();
		int maximumRedeliveries = ConfigReader.getCamelMaximumRedeliveries(); 
		int redeliveryDelay = ConfigReader.getCamelRedeliveryDelay();
		
		//
		// error handling
		//
		onException(Throwable.class)
			.maximumRedeliveries(maximumRedeliveries)
			.handled(true)
			.log("onException:: ${header.X-Correlation-ID} :: ${exception.message} :: ${exception}")
			.asyncDelayedRedelivery()
			.redeliveryDelay(redeliveryDelay)
			.onExceptionOccurred(new CustomErrorHandlerProcessor("onExceptionOccurred"))
			.onRedelivery(new CustomErrorHandlerProcessor("onRedelivery"))
			.process(new CustomErrorHandlerProcessor("onException"))
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
					.to(MngtConstants.MNGT_PRODUCER_DIRECT_ENDPOINT)
				.otherwise()
					.log("Stop consumers disabled by DISABLE_SUSPENSION environment variable.")
			.end();

		MetricsRoutePolicy mrp = MetricsFactory.createMetricsRoutePolicy(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID);

		//
		// consuming messages
		//
		from(ConfigReader.getQueueEndpoint())
			.routeId(ConsumerConstants.CONSUMER_RABBITMQ_ROUTE_ID)
			.routePolicy(mrp)
			.idempotentConsumer(header("X-Correlation-ID"), myIdempotentRepository)
			.to("direct:target");

		//
		// processing message
		//
		from("direct:target")
			.routeId(ConsumerConstants.CONSUMER_DIRECT_ROUTE_ID)
			.log("Message to send to upstream: ${header.X-Correlation-ID}")
			//start processing the message
			.choice()
				.when(header("test-scenario").isEqualTo("ko"))
					.throwException(new Exception("Some error happen!"))
			.end()
			//before deliver to backend/upstream
			.process(new BeforeSendToBackendProcessor())
			//delivering to backend/upstream
			.setHeader(HTTP_METHOD, constant(POST))
			.setHeader(HTTP_URI, constant(ConfigReader.getUpstreamEndpoint()))
			.to("http://oldhost?socketTimeout=2000")
			//just after deliver
			.process(new AfterSendToBackendProcessor())
			.log("Message sended: ${header.X-Correlation-ID}")
			//after here the ACK to RabbitMQ will happen
			;
		;
	}


}
