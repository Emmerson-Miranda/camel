Camel Java8 Router Project
==========================

Camel Java8 Router Project to access ActiveMQ (not Artemis). This project contains a simple example how to produce and consume messages from a queue.


To build this project use

    mvn install

To run this project from within Maven use

    mvn exec:java


Environment Parameters
------------------
```
activemq.connectionString=tcp://192.168.0.41:61616?soTimeout=60000
activemq.usr=guest
activemq.pwd=guest
activemq.queue=myqueue
publisher.timer.period.millis=1000
```

Run within Camel-k
------------------

Using kubectl/oc check Camel3 (if not change the camelVersion to 3.0.0-M2. Then redeploy the integration.)

```
$ kubectl/oc edit integrationplatform
```

To run integration inside camel-k passing parameters by command line



First register configuration properties using kubectl/oc


```
$ oc create configmap activemq-server  --from-file=MyActiveMQ.properties
```

Then run


```
$ kamel run -d camel-activemq --configmap=activemq-server --dev MyActiveMQConsumerRoute.java
```

Or (if you are using Camel3)

```
$ kamel run --dependency mvn:org.apache.camel/camel-activemq/3.0.0-M2 --dev MyActiveMQConsumerRoute.java
```



More info
===========

*   https://camel.apache.org/camel-maven-archetypes.html (see camel-archetype-java)
*   https://github.com/apache/camel/blob/master/components/camel-activemq/src/main/docs/activemq-component.adoc


