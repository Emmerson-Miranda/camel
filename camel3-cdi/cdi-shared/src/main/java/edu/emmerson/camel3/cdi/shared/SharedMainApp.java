package edu.emmerson.camel3.cdi.shared;

import org.apache.camel.cdi.Main;

public class SharedMainApp {
	
    public static void main(String... args) throws Exception {
    	System.out.println("------------------- Starting with shared main library -------------------");
        Main main = new Main();
        main.run(args);
        //main.close();
    }
    
}
