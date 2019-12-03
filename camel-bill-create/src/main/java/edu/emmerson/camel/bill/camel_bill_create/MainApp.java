package edu.emmerson.camel.bill.camel_bill_create;

import org.apache.camel.main.Main;
import org.apache.commons.lang3.StringUtils;

/**
 * A Camel Application
 */
public class MainApp {

    private static final String ENV_TIER = "ENV_TIER";
	private static final String ENV_NAME = "ENV_NAME";
	private static final String ENV_PROGRAMME = "ENV_PROGRAMME";

	/**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
    	
    	String programme = System.getenv(ENV_PROGRAMME);
    	String environment = System.getenv(ENV_NAME);
    	String tier = System.getenv(ENV_TIER);
    	
    	System.setProperty(ENV_PROGRAMME, StringUtils.isEmpty(programme) ? "demo" : programme);
    	
    	System.setProperty(ENV_NAME, StringUtils.isEmpty(environment) ? "poc" : environment);
    	
    	System.setProperty(ENV_TIER, StringUtils.isEmpty(tier) ? "dmz" : tier);

        Main main = new Main();
        main.addRoutesBuilder(new MyRouteBuilder());
        main.run(args);
    }
}

