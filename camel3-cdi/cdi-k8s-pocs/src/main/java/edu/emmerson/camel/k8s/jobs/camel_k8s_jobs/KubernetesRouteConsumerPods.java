package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kubernetes.KubernetesConstants;
import org.apache.camel.component.kubernetes.KubernetesOperations;

import io.fabric8.kubernetes.api.model.OwnerReference;
import io.fabric8.kubernetes.api.model.Pod;

public class KubernetesRouteConsumerPods extends RouteBuilder {

    
    @Override
    public void configure() {
    	//TODO: create kubernetesClient object
    	fromF("kubernetes-pods://{{kubernetes-master-url}}?oauthToken={{kubernetes-oauth-token:}}&namespace=default&resourceName=" + KubernetesCreateJob.JOB_NAME_PREFIX + "*")
    		.routeId(KubernetesRouteConsumerPods.class.getSimpleName())
    		.process(new KubernertesProcessor())
    		.choice()
    			.when(simple("${exchangeProperty.JOB_ACTION_DELETE} == 'true'"))
    				.log("REMOVING job ${header.CamelKubernetesJobName} from ${header.CamelKubernetesNamespaceName} namespace")
    				.toF("kubernetes-job:///{{kubernetes-master-url}}?oauthToken={{kubernetes-oauth-token:}}&operation=" + KubernetesOperations.DELETE_JOB_OPERATION)
    			.otherwise()
    				.log("KEEPING pod name: ${exchangeProperty.POD_NAME}")
    		.end()
    		.to("mock:result"); 
    }

    public class KubernertesProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            Message in = exchange.getIn();
            Pod pod = exchange.getIn().getBody(Pod.class);
            exchange.setProperty("POD_NAME", pod.getMetadata().getName());
            
            log.info("Got event from Pod: " + pod.getMetadata().getName() + ", action: " + in.getHeader(KubernetesConstants.KUBERNETES_EVENT_ACTION) + " Phase: " + pod.getStatus().getPhase());
            //[ https://192.168.64.3:8443/...] KubernetesRouteConsumerPods    INFO  Got event with configmap name: job-1599669499326-t82nh and action MODIFIED Phase: Pending
            //[ https://192.168.64.3:8443/...] KubernetesRouteConsumerPods    INFO  Got event with configmap name: job-1598015548964-vgqqn and action MODIFIED Phase: Succeeded
            //[ https://192.168.64.3:8443/...] KubernetesRouteConsumerPods    INFO  Got event with configmap name: job-1598015548964-vgqqn and action DELETED Phase: Succeeded
            
            
            List<OwnerReference> ors = pod.getMetadata().getOwnerReferences();
            ors.forEach(or ->{
            	if( or.getKind().equals("Job") ){
            		
            		//https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle/
            		//Pending, Running, Succeeded, Failed, Unknown
            		
            		String phase = pod.getStatus().getPhase();
            		if(!(phase.equals("Pending") || phase.equals("Running"))) {
            			//means the pahse is Succeeded, Failed, Unknown
            			//we proceed to remove the job
            			exchange.setProperty("JOB_ACTION_DELETE", Boolean.TRUE);
            			exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_JOB_NAME, or.getName());
            			exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_NAMESPACE_NAME, pod.getMetadata().getNamespace());
            		}
            		
            	}
            });
            
            //Map<String, String> labels = pod.getMetadata().getLabels();
            //labels.forEach((k,v) ->{log.info(k + "=" + v);});
        }	
    }
    
}