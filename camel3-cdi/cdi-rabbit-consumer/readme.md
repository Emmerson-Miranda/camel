# Camel 3 CDI - RabbitMQ Consumer


This example get messages from RabbitMQ.


## RabbitMQ
More info at: https://hub.docker.com/_/rabbitmq

To start RabbitMQ server run;

```
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
```

## Related projects
Camel 3 CDI - RabbitMQ Producer