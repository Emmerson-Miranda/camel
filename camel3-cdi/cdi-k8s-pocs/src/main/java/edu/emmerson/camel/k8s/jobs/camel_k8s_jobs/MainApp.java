package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.cdi.Main;
import org.apache.camel.component.properties.PropertiesComponent;

/**
 * https://kubernetes.io/docs/tasks/administer-cluster/access-cluster-api/
 * 
 * @author emmersonmiranda
 *
 */
public class MainApp {
	
	public static void main(String ...args) throws Exception {
		Main mainApp = new Main();
        mainApp.run(args);
	}
    
	
    @Produces
    @ApplicationScoped
    @Named("properties")
    // "properties" component bean that Camel uses to lookup properties
    PropertiesComponent properties() {
        PropertiesComponent component = new PropertiesComponent();
        component.setLocation("classpath:application.properties");
        return component;
    }

}
