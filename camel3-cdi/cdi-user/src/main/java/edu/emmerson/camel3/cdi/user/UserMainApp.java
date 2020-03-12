package edu.emmerson.camel3.cdi.user;

import org.apache.camel.cdi.Main;

public class UserMainApp {
	
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
        main.close();
    }
    
}
