# Camel 3 CDI - Error Handling


This example expose some REST interfaces and show some error handling scenarios


## RabbitMQ
More info at: https://hub.docker.com/_/rabbitmq

To start RabbitMQ server run;

```
docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672  rabbitmq:3-management
```


## Testing 

Run following commands drop a message in RabbitMQ, the headers will specify how the consumer is going to behave:

```

Run following commands drop a message in RabbitMQ, the headers will specify how the consumer is going to behave:

$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -X POST http://0.0.0.0:8080/eh/noerror


$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -X POST http://0.0.0.0:8080/eh/in

$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -X POST http://0.0.0.0:8080/eh/inoe




```

