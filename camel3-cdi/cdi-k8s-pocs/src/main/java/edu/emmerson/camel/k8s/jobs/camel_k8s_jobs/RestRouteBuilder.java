package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs;

import org.apache.camel.builder.RouteBuilder;

public class RestRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		

		restConfiguration().component("undertow")
        .contextPath("/").host("localhost").port("8080")
        // add swagger api-doc out of the box
        .apiContextPath("/api-doc")
        	.apiContextRouteId("api-doc-endpoint")
            // and enable CORS
            .apiProperty("cors", "true");
		
		rest("/k8s").description("K8S operations.")
	        .get("/pod").description("List pods.")
	            .route().routeId("LIST-POD-REST-GET")
	            .to(KubernetesRouteListPods.FROM).endRest()
	        .get("/job").description("List jobs.")
	            .route().routeId("LIST-JOB-REST-GET")
	            .to(KubernetesListJob.FROM).endRest()
	         .post("/job").description("Create a simple job.")
	            .route().routeId("CREATE-JOB-REST-POST")
	            .to(KubernetesCreateJob.FROM)
        ;
		
		
		
	}

}
