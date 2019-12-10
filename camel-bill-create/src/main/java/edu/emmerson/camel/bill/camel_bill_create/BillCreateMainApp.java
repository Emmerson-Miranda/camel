package edu.emmerson.camel.bill.camel_bill_create;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class BillCreateMainApp {

    public static void main(String... args) throws Exception {
    	
    	EnvVars.setDefaultEnvVars(BillCreateMainApp.class.getSimpleName());

        Main main = new Main();
        main.addRoutesBuilder(new BillCreateRouteBuilder());
        main.run(args);
        main.close();
    }
}

