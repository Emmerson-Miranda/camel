package edu.emmerson.camel.java8.activemq.camel_java8_activemq;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        
        System.setProperty("activemq.connectionString", "tcp://192.168.0.41:61616?soTimeout=60000");
        System.setProperty("activemq.usr", "guest");
        System.setProperty("activemq.pwd", "guest");
        System.setProperty("activemq.queue", "myqueue");
        System.setProperty("publisher.timer.period.millis", "1000");
        
        main.addRouteBuilder(new MyActiveMQConsumerRoute());
        main.addRouteBuilder(new MyActiveMQProducerRoute());
        main.run(args);
    }
}

