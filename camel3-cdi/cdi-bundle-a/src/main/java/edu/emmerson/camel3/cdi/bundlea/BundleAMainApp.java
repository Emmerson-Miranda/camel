package edu.emmerson.camel3.cdi.bundlea;

import org.apache.camel.cdi.Main;

public class BundleAMainApp {
	
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
        main.close();
    }
    
}
