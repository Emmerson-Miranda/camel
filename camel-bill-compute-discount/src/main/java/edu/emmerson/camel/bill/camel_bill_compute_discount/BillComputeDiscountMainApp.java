package edu.emmerson.camel.bill.camel_bill_compute_discount;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class BillComputeDiscountMainApp {


    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {

    	EnvVars.setDefaultEnvVars(BillComputeDiscountMainApp.class.getSimpleName());

        Main main = new Main();
        main.addRoutesBuilder(new BillComputeDiscountRouteBuilder());
        main.run(args);
        main.close();
    }
}

