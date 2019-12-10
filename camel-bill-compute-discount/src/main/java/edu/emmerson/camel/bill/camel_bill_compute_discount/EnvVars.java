package edu.emmerson.camel.bill.camel_bill_compute_discount;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class EnvVars {
	
    private static final String ENV_TIER = "ENV_TIER";
	private static final String ENV_NAME = "ENV_NAME";
	private static final String ENV_PROGRAMME = "ENV_PROGRAMME";
	private static final String ENV_APP_ID = "ENV_APP_ID";
	private static final String HOSTNAME = "HOSTNAME";
	
	private static List<String> ENV_VARS = new ArrayList<String>();
	static {
		//ENV_VARS.add(ENV_APP_ID);
		ENV_VARS.add(ENV_NAME);
		ENV_VARS.add(ENV_PROGRAMME);
		ENV_VARS.add(ENV_TIER);
		ENV_VARS.add(HOSTNAME);
	}

	public static void setDefaultEnvVars(String appName) {
    	String programme = System.getenv(ENV_PROGRAMME);
    	String environment = System.getenv(ENV_NAME);
    	String tier = System.getenv(ENV_TIER);
    	String host = System.getenv(HOSTNAME);
    	
    	System.setProperty(ENV_PROGRAMME, StringUtils.isEmpty(programme) ? "demo" : programme);
    	System.setProperty(ENV_NAME, StringUtils.isEmpty(environment) ? "local" : environment);
    	System.setProperty(ENV_TIER, StringUtils.isEmpty(tier) ? "dmz" : tier);
    	System.setProperty(ENV_APP_ID, appName);
    	System.setProperty(HOSTNAME, StringUtils.isEmpty(host) ? "localhost" : host);
	}
	
	public static String checkRequiredEnvVars() {
		StringBuilder output = new StringBuilder();
		for(String env : ENV_VARS) {
			if(StringUtils.isEmpty(System.getenv(env))) {
				output.append("Missing property ").append(env).append(". ");
			}
		}
		return output.toString();
	}
}
