package edu.emmerson.camel3.cdi.rmq;

import java.util.jar.Manifest;

import org.apache.camel.api.management.JmxSystemPropertyKeys;
import org.apache.camel.cdi.Main;

public class ConsumerMainApp {
	
    public static void main(String... args) throws Exception {
    	
    	Manifest mf = new Manifest(ConsumerMainApp.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
        System.out.println(mf.getAttributes("Main-Class"));
        System.out.println(mf.getAttributes("Class-Path"));
        
    	System.out.println("------------------- Starting with shared main library -------------------");
    	
    	System.setProperty(JmxSystemPropertyKeys.DISABLED, "false");
    	
        Main main = new Main();

        main.run(args);
        
        
        //main.close();
    }
    
}
