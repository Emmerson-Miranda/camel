package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kubernetes.KubernetesOperations;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientException;

public class KubernetesRouteListPods extends RouteBuilder {

	public static final String FROM = "direct:" + KubernetesRouteListPods.class.getSimpleName();

    @Override
    public void configure() {
        from(FROM)
        	.routeId("kubernetes-pods-client")
            .onException(KubernetesClientException.class).handled(true)
                .log(LoggingLevel.ERROR, "${exception.message}")
                .log("Stopping the Kubernetes route...")
                // Let's stop the route (we may want to implement a way to exit the container)
                .to("controlbus:route?routeId=kubernetes-client&action=stop&async=true&loggingLevel=DEBUG")
                .end()
            .to("kubernetes-pods://{{kubernetes-master-url}}?oauthToken={{kubernetes-oauth-token:}}&operation=" + KubernetesOperations.LIST_PODS_OPERATION)
            .log("We currently have ${body.size()} pods:")
            .process(exchange -> {
                @SuppressWarnings("unchecked")
				List<Pod> pods = exchange.getIn().getBody(List.class);
                // Compute the length of the longer pod name
                String tty = "%-" + (pods.stream().mapToInt(pod -> pod.getMetadata().getName().length()).max().orElse(30) + 2) + "s %-9s %-9s %-10s %s";
                // Emulates the output of 'kubectl get pods'
                System.out.println(String.format(tty, "NAME", "READY", "STATUS", "RESTARTS", "AGE"));
                pods.stream()
                    .map(pod -> String.format(tty, pod.getMetadata().getName(),
                        pod.getStatus().getContainerStatuses().stream()
                            .filter(ContainerStatus::getReady)
                            .count() + "/" + pod.getStatus().getContainerStatuses().size(),
                        pod.getStatus().getPhase(),
                        pod.getStatus().getContainerStatuses().stream()
                            .mapToInt(ContainerStatus::getRestartCount).sum(),
                        formatDuration(Duration.between(ZonedDateTime.parse(pod.getStatus().getStartTime()), ZonedDateTime.now()))))
                    .forEach(System.out::println);
            });
    }
    
    // Let's format duration the kubectl way!
    static String formatDuration(Duration duration) {
        if (Duration.ofDays(1).compareTo(duration) < 0) {
            return duration.toDays() + "d";
        } else if (Duration.ofHours(1).compareTo(duration) < 0) {
            return duration.toHours() + "h";
        } else if (Duration.ofMinutes(1).compareTo(duration) < 0) {
            return duration.toMinutes() + "m";
        } else {
            return duration.getSeconds() + "s";
        }
    }
    
}