# Camel 3 CDI - RabbitMQ Consumer


This example get messages from RabbitMQ.


## RabbitMQ
More info at: https://hub.docker.com/_/rabbitmq

To start RabbitMQ server run;

```
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
```

## Testing

Stop consuming messages from queue

```
$ curl -d "{\"routeId\": \"edu.emmerson.camel3.cdi.rmq.ConsumerRouteBuilder-Consumer\", \"routingKey\": \"rabbit.consumer\", \"suspend\": \"true\"}" -H "Content-Type: application/json"  -X POST http://0.0.0.0:9090/mngt/publish
```

Start consuming messages from queue

```
$ curl -d "{\"routeId\": \"edu.emmerson.camel3.cdi.rmq.ConsumerRouteBuilder-Consumer\", \"routingKey\": \"rabbit.consumer\", \"suspend\": \"false\"}" -H "Content-Type: application/json"  -X POST http://0.0.0.0:9090/mngt/publish
```

Gracefull shudown

```
$ curl http://0.0.0.0:9090/mngt/shutdown
```

## Related projects
Camel 3 CDI - RabbitMQ Producer