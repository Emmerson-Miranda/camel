package edu.emmerson.camel3.cdi.blenda;

import org.apache.camel.cdi.Main;

public class BlendAMainApp {
	
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
        main.close();
    }
    
}
