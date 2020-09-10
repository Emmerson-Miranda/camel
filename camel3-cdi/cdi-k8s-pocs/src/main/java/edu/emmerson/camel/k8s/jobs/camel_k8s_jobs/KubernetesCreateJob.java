package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Uri("log:output")
    private Endpoint resultEndpoint;
    
    public static final String FROM = "direct:" + KubernetesCreateJob.class.getSimpleName();
    
    public static final String JOB_NAME_PREFIX = "job-";

    @Override
    public void configure() {
        from(FROM)
        	.routeId("kubernetes-jobcreate-client")
        	.process(exchange -> {
        		exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_JOB_NAME, JOB_NAME_PREFIX + System.currentTimeMillis()); //DNS-1123 subdomain must consist of lower case alphanumeric characters, '-' or '.', and must start and end with an alphanumeric character (e.g. 'example.com', regex used for validation is '[a-z0-9]([-a-z0-9]*[a-z0-9])?(\.[a-z0-9]([-a-z0-9]*[a-z0-9])?)*')
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
		
		//js.setBackoffLimit(3);
		js.setCompletions(1);
		Long _2horas = new Long((60*60*2));
		js.setActiveDeadlineSeconds(_2horas); //kill the pods if the pod exceeds the 4h running
		Integer ttlSecondsAfterFinished = (60 * 10); //(60*60*4); //4 hours
		js.setTtlSecondsAfterFinished(ttlSecondsAfterFinished );
		
		PodTemplateSpec pts = new PodTemplateSpec();
		
		PodSpec ps = new PodSpec();
		ps.setRestartPolicy("OnFailure");
		ps.setContainers(generateContainers());

		//ps.setVolumes(getVolumes());
		
		ObjectMeta metadata = new ObjectMeta();
		Map<String, String> annotations = new HashMap<String, String>();
		annotations.put("jobMetadataAnnotation1", "random value");
		metadata.setAnnotations(annotations);
		
		Map<String, String> podlabels = new HashMap<String, String>();
		podlabels.put("podLabelKey1", "value1");
		podlabels.put("podLabelKey2", "value2");
		podlabels.put("orderId", UUID.randomUUID().toString());
		podlabels.put("app", "podFromCamelApp");
		metadata.setLabels(podlabels);
		
		pts.setSpec(ps);
		pts.setMetadata(metadata);
		js.setTemplate(pts);
		return js;
	}

	/*
	private List<Volume> getVolumes() {
		List<Volume> volumes = new ArrayList<Volume>();
		
		Volume v = new Volume();
		v.setName("task-pv-storage");
		PersistentVolumeClaimVolumeSource pvc = new PersistentVolumeClaimVolumeSource();
		pvc.setClaimName("task-pv-claim");
		pvc.setReadOnly(false);
		v.setPersistentVolumeClaim(pvc);
		volumes.add(v);
		
		return volumes;
	}
	*/

	private List<Container> generateContainers() {
		Container container = new Container();
		container.setName("pi");
		container.setImage("perl");

		
		List<String> command = new ArrayList<String>();
		command.add("echo");
		command.add("Job created from Apache Camel code at " + (new Date()));
		
		
		//command.add("cat");
		//command.add("/myfolder/index.html");
		
		//command.add("/myfolder/startup.sh");

		//command.add(" && ");
		/*
		command.add("ls");
		command.add("/bin/sh");
		List<String> args = new ArrayList<String>();
		args.add("-c");
		args.add("echo \"Hello from the debian container\" > /opt/myfolder/index.html");
		container.setArgs(args );
		*/
		container.setCommand(command);
		
		/*
		//
		//This section can be replaced by mounting withing the container any external storage system. 
		//
		List<VolumeMount> volumeMounts = new ArrayList<VolumeMount>();
		VolumeMount vm = new VolumeMount();
		vm.setName("task-pv-storage");
		vm.setMountPath("/myfolder");
		volumeMounts.add(vm);
		container.setVolumeMounts(volumeMounts);
		*/
		
		List<Container> containers = new ArrayList<Container>();
		containers.add(container);
		return containers;
	}

}
