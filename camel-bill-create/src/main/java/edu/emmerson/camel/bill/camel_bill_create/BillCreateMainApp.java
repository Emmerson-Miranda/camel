package edu.emmerson.camel.bill.camel_bill_create;

import org.apache.camel.main.Main;
import org.apache.commons.lang3.StringUtils;


/**
 * A Camel Application
 */
public class BillCreateMainApp {

    private static final String ENV_TIER = "ENV_TIER";
	private static final String ENV_NAME = "ENV_NAME";
	private static final String ENV_PROGRAMME = "ENV_PROGRAMME";
	private static final String ENV_APP_ID = "ENV_APP_ID";
	private static final String ENV_DISCOUNT_BACKEND_URL = "ENV_DISCOUNT_BACKEND_URL";

    public static void main(String... args) throws Exception {
    	
    	String programme = System.getenv(ENV_PROGRAMME);
    	String environment = System.getenv(ENV_NAME);
    	String tier = System.getenv(ENV_TIER);
    	
    	System.setProperty(ENV_PROGRAMME, StringUtils.isEmpty(programme) ? "demo" : programme);
    	System.setProperty(ENV_NAME, StringUtils.isEmpty(environment) ? "local" : environment);
    	System.setProperty(ENV_TIER, StringUtils.isEmpty(tier) ? "dmz" : tier);
    	System.setProperty(ENV_APP_ID, BillCreateMainApp.class.getName());
    	if (System.getProperty(ENV_DISCOUNT_BACKEND_URL) == null) {
			System.setProperty(ENV_DISCOUNT_BACKEND_URL, "undertow:http://127.0.0.1:8090/proxy/demo/discount");
		}

        Main main = new Main();
        main.addRoutesBuilder(new BillCreateRouteBuilder());
        main.run(args);
        main.close();
    }
}

