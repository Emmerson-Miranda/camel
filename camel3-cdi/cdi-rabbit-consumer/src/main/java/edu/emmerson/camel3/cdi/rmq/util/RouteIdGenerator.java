package edu.emmerson.camel3.cdi.rmq.util;

public final class RouteIdGenerator {
	
	public static enum RouteType{
		CAMEL_DIRECT("camel.direct"),
		AMQP_CONSUMER("amqp.consumer"),
		AMQP_PRODUCER("amqp.producer"),
		HTTP_GET("http.get"),
		HTTP_POST("http.post"),
		HTTP_PUT("http.put"),
		HTTP_DELETE("http.delete"),
		HTTP_HEAD("http.head"),
		HTTP_PATCH("http.patch");
		
		private final String value;  
		
		RouteType(String val) {
			this.value = val;
		}
		
		public String toString() {
			return this.value;
		}
	}
	
	/**
	 * Generate Camel Route IDs following an specific pattern.
	 * 
	 * @param msCode microservice Id or code name (use to be a short value)
	 * @param resourceName In case of REST endpoint the HTTP resource; In case of JMS/AMQP can be the queue name.
	 * @param routeType
	 * @return
	 */
	public static String newRouteId(String msCode, String resourceName, RouteType routeType) {
		StringBuilder sb = new StringBuilder();
		sb.append("ms.").append(msCode)
			.append(".").append(resourceName)
			.append(".").append(routeType);
		return sb.toString().toLowerCase();
	}

}
