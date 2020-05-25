package edu.emmerson.camel3.cdi.rmq;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(CamelCdiRunner.class)
public class CamelCdiTest {
	
    public static final String PUBLISH_MESSAGE = "direct:publishMessage";
    
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9999);
    
    @Inject
    CamelContext context;
    
    @Test
    public void publish_test_200(@Uri(PUBLISH_MESSAGE) ProducerTemplate producer) {
    	stubFor(post(urlEqualTo("/myService"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody("Happy path response.")));
    	
    	String msg = "{\"key\" : \"value\" }";
    	producer.sendBodyAndHeader(msg, "test-scenario", "ok");
    }
    
    @Test
    public void publish_test_400(@Uri(PUBLISH_MESSAGE) ProducerTemplate producer) {
    	stubFor(post(urlEqualTo("/myService"))
                .willReturn(aResponse()
                    .withStatus(400)
                    .withBody("<response>Some content</response>")));
    	
    	String msg = "{\"key\" : \"value\" }";
    	producer.sendBodyAndHeader(msg, "test-scenario", "ok");
    }

    /*
    @Test
    public void mngtPublish_test_ok(@Uri(PUBLISH_MESSAGE) ProducerTemplate producer) {
    	String msg = "{\"key\" : \"value\" }";
    	producer.sendBodyAndHeader(msg, "test-scenario", "ok");
    }
    */
    
}
