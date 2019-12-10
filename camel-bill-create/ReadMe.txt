Camel Java8 Router Project
==========================

To build this project use

    mvn install

To run this project from within Maven use

    mvn exec:java

For more help see the Apache Camel documentation

    http://camel.apache.org/

curl examples
-------------

curl -d '{"amount" : "350.00", "customerType" : "A"}' -H "Content-Type: application/json" -H "X-Request-ID: aaaa-bbbb-cccc-dddd" -X POST http://0.0.0.0:8080/proxy/demo/bill

curl http://0.0.0.0:8080/proxy/demo/prove/readiness

curl http://0.0.0.0:8080/proxy/demo/prove/liveness


Start container
---------------

docker run --env ENV_NAME=myenv --env ENV_PROGRAMME=myprog --env ENV_TIER=mytier --env ENV_DISCOUNT_BACKEND_URL=undertow:http://0.0.0.0:8090/proxy/compute -p 8080:8080 camel-bill-create:1.1.0 
