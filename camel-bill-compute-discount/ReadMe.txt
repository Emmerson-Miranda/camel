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

curl -d '{"amount" : "150.00", "customerType" : "A"}' -H "Content-Type: application/json" -H "X-Request-ID: aaaa-bbbb-cccc-dddd" -X POST http://0.0.0.0:8090/proxy/compute/discount

curl http://0.0.0.0:8090/proxy/compute/prove/readiness

curl http://0.0.0.0:8090/proxy/compute/prove/liveness


Start container
---------------

docker run --env ENV_NAME=myenv --env ENV_PROGRAMME=myprog --env ENV_TIER=mytier -p 8090:8090 camel-bill-compute-discount:1.1.0 
