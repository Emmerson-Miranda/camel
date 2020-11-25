package edu.emmerson.camel3.cdi.rmq;

import org.apache.camel.api.management.JmxSystemPropertyKeys;
import org.apache.camel.cdi.Main;

import edu.emmerson.camel3.cdi.rmq.util.ConfigReader;

public class ConsumerMainApp {
	
	/**
	 * Before run main method
	 * 1 - Start rabbitmq docker - docker run -d --hostname rabbitmq --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3-management
	 * 2 - Start upstream docker - docker run -d -p 10003:10003 --name upstream-mock upstream:mock
	 * 3 - Start envoy docker    - see envoy/readme.md
	 * @param args
	 * @throws Exception
	 */
    public static void main(String... args) throws Exception {
    	
    	//Manifest mf = new Manifest(ConsumerMainApp.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
        //System.out.println(mf.getMainAttributes().getValue("Main-Class"));
        //System.out.println(mf.getMainAttributes().getValue("Class-Path"));
        
    	System.out.println("------------------- Starting with shared main library -------------------");
    	
    	
    	System.setProperty(JmxSystemPropertyKeys.DISABLED, "false");
    	
    	System.setProperty(ConfigReader.RABBIT_PORT, "5672");
    	System.setProperty(ConfigReader.RABBIT_HOST, "localhost");
    	System.setProperty(ConfigReader.RABBIT_CLIENT_SLEEP_ON_DISCONNECTION_ENABLE, "false");
    	System.setProperty(ConfigReader.DISABLE_SUSPENSION, "true");
    	
    	
        Main main = new Main();

        main.run(args);
        
        
        //main.close();
    }
    
}
