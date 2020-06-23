package edu.emmerson.camel3.cdi.rmq;

public class ProducerMainApp {
	
	/**
	 * Before run main method
	 * 1 - Start rabbitmq docker - docker run -d --hostname rabbitmq --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3-management
	 * @param args
	 * @throws Exception
	 */
    public static void main(String... args) throws Exception {
    	System.out.println("------------------- Starting with local main ProducerMainApp -------------------");
        
    	System.setProperty(ConfigReader.RABBIT_PORT, "5672");
    	System.setProperty(ConfigReader.RABBIT_HOST, "localhost");
    	
    	edu.emmerson.camel3.cdi.shared.SharedMainApp.main(args);
    }
    
}
