# Camel 3 CDI - RabbitMQ Producer


This example expose a REST interface and save the POST payload as a message in RabbitMQ


## RabbitMQ
More info at: https://hub.docker.com/_/rabbitmq

To start RabbitMQ server run;

```
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
```


## Testing 

Run following commands:

```
$ curl -d "{\"ok\": \"value without error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID1" -H "test-scenario: ok" -X POST http://0.0.0.0:8080/rmq/publish

$ curl -d "{\"ko\": \"value with error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID6" -H "test-scenario: ko" -X POST http://0.0.0.0:8080/rmq/publish

```


## Related projects
Camel 3 CDI - RabbitMQ Consumer