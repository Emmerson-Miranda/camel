package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.PropertyInject;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Bean factory
 * @author emmersonmiranda
 *
 */
public class MyFactoryBean  {

	@PropertyInject("kubernetes-master-url")
	String masterUrl;
	
	@PropertyInject("kubernetes-oauth-token")
	String token;
	
	@Produces
	@ApplicationScoped
	@Named("myKubernetesClient")
	public KubernetesClient createDefaultKubernetesClient() {
		//TODO: PENDING TO BE IMPLEMENTED PROPERLY
		Config config = null;
		return new DefaultKubernetesClient(config);
	}

}
