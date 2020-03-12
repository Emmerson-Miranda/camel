package edu.emmerson.camel3.cdi.address;

import org.apache.camel.cdi.Main;

public class AddressMainApp {
	
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
        main.close();
    }
    
}
