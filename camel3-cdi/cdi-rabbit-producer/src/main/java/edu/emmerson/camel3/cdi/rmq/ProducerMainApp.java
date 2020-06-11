package edu.emmerson.camel3.cdi.rmq;

public class ProducerMainApp {
	
    public static void main(String... args) throws Exception {
    	System.out.println("------------------- Starting with local main ProducerMainApp -------------------");
        
    	System.setProperty(ConfigReader.RABBIT_PORT, "25672");
    	System.setProperty(ConfigReader.RABBIT_HOST, "localhost");
    	
    	edu.emmerson.camel3.cdi.shared.SharedMainApp.main(args);
    }
    
}
