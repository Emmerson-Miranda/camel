# Camel 3 CDI - RabbitMQ Producer


This example expose a REST interface and save the POST payload as a message in RabbitMQ


## RabbitMQ
More info at: https://hub.docker.com/_/rabbitmq

To start RabbitMQ server run;

```
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
```


## Testing 

Run following commands drop a message in RabbitMQ, the headers will specify how the consumer is going to behave:

```
$ curl -d "{\"ok\": \"value without error, upstream return 404\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ok"  -X POST http://0.0.0.0:8080/rmq/publish

$ curl -d "{\"ok\": \"value with error, camel raise an exception internally\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ko" -H "X-US-SCENARIO: 200"  -X POST http://0.0.0.0:8080/rmq/publish

$ curl -d "{\"ok\": \"value without error, upstream return 200\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ok" -H "X-US-SCENARIO: 200"  -X POST http://0.0.0.0:8080/rmq/publish

$ curl -d "{\"ok\": \"value without error, raise a 400 error in the upstream\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ok" -H "X-US-SCENARIO: 400"  -X POST http://0.0.0.0:8080/rmq/publish

$ curl -d "{\"ok\": \"value without error, raise a 500 error in the upstream\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ok" -H "X-US-SCENARIO: 500"  -X POST http://0.0.0.0:8080/rmq/publish

```


## Related projects
Camel 3 CDI - RabbitMQ Consumer