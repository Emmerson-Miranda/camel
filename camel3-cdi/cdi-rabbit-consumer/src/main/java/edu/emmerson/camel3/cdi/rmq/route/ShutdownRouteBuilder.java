package edu.emmerson.camel3.cdi.rmq.route;

import java.time.Instant;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

import edu.emmerson.camel3.cdi.rmq.controlbus.MngtConstants;
import edu.emmerson.camel3.cdi.rmq.util.ConfigReader;

/**
 * 
 * @author emmersonmiranda
 * @link https://camel.apache.org/manual/latest/graceful-shutdown.html
 */
public class ShutdownRouteBuilder extends RouteBuilder {

	@Inject
	CamelContext context;

	@Override
	public void configure() throws Exception {

		// Handling SIGTERM gracefully
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				gracefullShutdown(context);
			}
		});

		from(MngtConstants.MNGT_SHUTDOWN_DIRECT_ENDPOINT).startupOrder(10000)
				.routeId(MngtConstants.MNGT_SHUTDOWN_DIRECT_ROUTE_ID)
				.log("shuting down camel at " + Instant.now().toString()).process().message(m -> {
					gracefullShutdown(m.getExchange().getContext());
				});
	}

	/**
	 * Shutdown camel context.
	 * 
	 * @param context
	 */
	private static void gracefullShutdown(CamelContext context) {

		final long shutdownTimeout = ConfigReader.getShutdownTimeout();

		System.out.println("************************************************");
		System.out.println("************************************************");
		System.out.println("Start Exiting Camel Gracefully");
		System.out.println("************************************************");
		System.out.println("************************************************");

		if (shutdownTimeout > -1) {
			// give seconds to shutdown
			System.out.println("Shutdown timeout " + shutdownTimeout + " secs.");
			context.getShutdownStrategy().setTimeout(shutdownTimeout);
		}

		context.shutdown();
		System.out.println("************************************************");
		System.out.println("************************************************");
		System.out.println("Exiting Camel Fisnished");
		System.out.println("************************************************");
		System.out.println("************************************************");
	}

}
