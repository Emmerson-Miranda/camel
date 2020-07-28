package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.kubernetes.KubernetesConstants;
import org.apache.camel.component.kubernetes.KubernetesOperations;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.batch.JobSpec;

/**
 * 
 */
public class KubernetesCreateJob extends RouteBuilder {

    @Inject
    @Uri("timer:foo?delay=1000&repeatCount=1")
    private Endpoint inputEndpoint;

    @Inject
    @Uri("log:output")
    private Endpoint resultEndpoint;

    @Override
    public void configure() {
        // you can configure the route rule with Java DSL here

        from(inputEndpoint)
        	.routeId("kubernetes-jobcreate-client")
        	.process(exchange -> {
        		exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_JOB_NAME, "camel-job"); //DNS-1123 subdomain must consist of lower case alphanumeric characters, '-' or '.', and must start and end with an alphanumeric character (e.g. 'example.com', regex used for validation is '[a-z0-9]([-a-z0-9]*[a-z0-9])?(\.[a-z0-9]([-a-z0-9]*[a-z0-9])?)*')
                exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, "default");
                
                Map<String, String> joblabels = new HashMap<String, String>();
                joblabels.put("jobLabelKey1", "value1");
                joblabels.put("jobLabelKey2", "value2");
                joblabels.put("app", "jobFromCamelApp");
                exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_JOB_LABELS, joblabels);

                exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_JOB_SPEC, generateJobSpec());
        	})
        	.toF("kubernetes-job:///{{kubernetes-master-url}}?oauthToken={{kubernetes-oauth-token:}}&operation=" + KubernetesOperations.CREATE_JOB_OPERATION)
        	.log("Job created:")
        	.process(exchange -> {
        		System.out.println(exchange.getIn().getBody());
        	})
            .to(resultEndpoint);
    }

    /**
     * Create a new JobSpec
     * @return
     */
	private JobSpec generateJobSpec() {
		JobSpec js = new JobSpec();
		
		PodTemplateSpec pts = new PodTemplateSpec();
		
		PodSpec ps = new PodSpec();
		ps.setRestartPolicy("Never");
		ps.setContainers(generateContainers());
		pts.setSpec(ps);
		
		ObjectMeta metadata = new ObjectMeta();
		Map<String, String> annotations = new HashMap<String, String>();
		annotations.put("jobMetadataAnnotation1", "random value");
		metadata.setAnnotations(annotations);
		
		Map<String, String> podlabels = new HashMap<String, String>();
		podlabels.put("podLabelKey1", "value1");
		podlabels.put("podLabelKey2", "value2");
		podlabels.put("app", "podFromCamelApp");
		metadata.setLabels(podlabels);
		
		pts.setMetadata(metadata);
		js.setTemplate(pts);
		return js;
	}

	private List<Container> generateContainers() {
		Container container = new Container();
		container.setName("pi");
		container.setImage("perl");
		List<String> command = new ArrayList<String>();
		command.add("echo");
		command.add("Job created from Apache Camel code at " + (new Date()));
		container.setCommand(command);
		List<Container> containers = new ArrayList<Container>();
		containers.add(container);
		return containers;
	}

}
