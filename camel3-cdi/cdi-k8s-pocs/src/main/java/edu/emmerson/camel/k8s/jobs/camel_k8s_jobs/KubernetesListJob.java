package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import java.util.List;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.kubernetes.KubernetesOperations;

import io.fabric8.kubernetes.api.model.batch.Job;

/**
 * 
 */
public class KubernetesListJob extends RouteBuilder {

    @Inject
    @Uri("timer:foo?delay=4000&repeatCount=1")
    private Endpoint inputEndpoint;

    @Inject
    @Uri("log:output")
    private Endpoint resultEndpoint;

    @Override
    public void configure() {

        from(inputEndpoint)
        	.routeId("kubernetes-joblist-client") 
        	.toF("kubernetes-job:///{{kubernetes-master-url}}?oauthToken={{kubernetes-oauth-token:}}&operation=" + KubernetesOperations.LIST_JOB)
        	.log("We currently have ${body.size()} jobs:")
        	.process(exchange -> {
        		List<Job> jobs = exchange.getIn().getBody(List.class);
        		jobs.forEach( j -> {
        			//System.out.println(j);
        			System.out.println(j.getMetadata().getName());
        			System.out.println(j.getMetadata().getNamespace());
        			System.out.println(j.getMetadata().getLabels());
        			System.out.println(j.getSpec().getBackoffLimit());
        			System.out.println(j.getSpec().getCompletions());
        			//System.out.println(j.getSpec().getTemplate());
        			System.out.println(j.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
        			System.out.println(j.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv());
        			System.out.println("-------------------------------------");
        			System.out.println(j);
        			System.out.println("-------------------------------------");
        			//KubernetesConstants.KUBERNETES_JOB_SPEC;
        		});
        	})
            .to(resultEndpoint);
    }

}
