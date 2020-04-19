package edu.emmerson.camel3.cdi.rmq;

import org.apache.camel.api.management.JmxSystemPropertyKeys;
import org.apache.camel.cdi.Main;

public class ConsumerMainApp {
	
    public static void main(String... args) throws Exception {
    	System.out.println("------------------- Starting with shared main library -------------------");
    	
    	System.setProperty(JmxSystemPropertyKeys.DISABLED, "false");
    	
        Main main = new Main();

        main.run(args);
        
        //main.close();
    }
    
}
