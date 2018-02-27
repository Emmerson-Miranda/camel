package edu.emmerson.camel.springboot.helloworld;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("hello")
public class HelloBean {

    @Autowired
    CamelContext context;

    public String greetings(String name) {
        return "Greetings for " + name + ", I'm " + context + "!";
    }
    
    public String sayHello(@Header("name") String name) {
        return "Hello " + name + ", I'm " + context + "!";
    }
    
    public Message sayHelloObject(@Header("name") String name) {
        return new Message("Hello " + name + ", I'm " + context + "!");
    }
    
    
}
