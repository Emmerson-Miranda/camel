package edu.emmerson.camel.java8.vault.camel_java8_vault_client;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
    	
    	if(System.getProperty(Constants.PARAM_VAULT_TOKEN) == null) {
    		System.setProperty(Constants.PARAM_VAULT_TOKEN, "s.vvMr4cJCBT8rccg6Z5DfCuZK");
    	}
    	
    	if(System.getProperty(Constants.PARAM_VAULT_URL) == null) {
    		System.setProperty(Constants.PARAM_VAULT_URL, "http://127.0.0.1:8200");
    	}
    	
        Main main = new Main();
        main.addRouteBuilder(new MyRouteBuilder());
        main.run(args);
    }
}

