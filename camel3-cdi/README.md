# Introduction
This folder contains some demos to demostrate how to create Camel 3 microservices using Camel CDI (avoiding Spring Boot like applications).


N.B:
* All APIs expose OpenAPI (Swagger) specification.
  * In bundle and blend projects all swagger definitions mix-in at runtime.
* All APIs are using same IP and port.
  * In bundle and blend projects they mix-in at runtime without conflicts under the same port.
* When an image is generated, not fat (uber) jar is generated.
* You can create your own library to *share* components across different projects (cdi-bundle), CDI will discover the components in runtime.
* Please have a look at https://camel.apache.org/components/latest/cdi.html.

# The microservices

## cdi-k8s-pocs
Pure Camel 3 CDI microservice tha provides kubernetes integration.
[source code](./cdi-k8s-pocs/readme.md)

## cdi-user
Pure Camel 3 CDI microservice tha provides dummy user information.

```
INFO  Route: route1 started and consuming from: http://0.0.0.0:8080/api-doc
INFO  Route: route2 started and consuming from: http://0.0.0.0:8080/user/findAll
```

### Building and running the image
Go to the root of your java project and run
```
mvn clean package docker:build
```
Runing the image
```
docker run -d -p 8080:8080 cdi-user:1.0-SNAPSHOT
```
Watch logs
```
docker logs $(docker ps | grep "cdi-user" | awk '{print $1}') -f
````
Stop the image
```
docker stop $(docker ps | grep "cdi-user" | awk '{print $1}')
```


## cdi-address
Pure Camel 3 CDI microservice tha provides dummy address information.
```
INFO  Route: route1 started and consuming from: http://0.0.0.0:8080/api-doc
...
INFO  Route: route2 started and consuming from: direct://myAddressService
INFO  Route: route3 started and consuming from: http://0.0.0.0:8080/address/findAll
...
```

### Building and running the image
Go to the root of your java project and run
```
mvn clean package docker:build
```
Runing the image
```
docker run -d -p 8080:8080 cdi-address:1.0-SNAPSHOT
```
Watch logs
```
docker logs $(docker ps | grep "cdi-address" | awk '{print $1}') -f
````
Stop the image
```
docker stop $(docker ps | grep "cdi-address" | awk '{print $1}')
```

## cdi-bundle-a
Pure Camel 3 CDI microservice that bundle cdi-user and cdi-address reusing the original components.

```
...
INFO  Route: route1 started and consuming from: http://0.0.0.0:8080/api-doc
INFO  Route: route2 started and consuming from: http://0.0.0.0:8080/user/findAll
INFO  Route: route3 started and consuming from: direct://myAddressService
INFO  Route: route4 started and consuming from: http://0.0.0.0:8080/address/findAll
```

### Building and running the image
Go to the root of your java project and run
```
mvn clean package docker:build
```
Runing the image
```
docker run -d -p 8080:8080 cdi-bundlea:1.0-SNAPSHOT
```
Watch logs
```
docker logs $(docker ps | grep "cdi-bundlea" | awk '{print $1}') -f
````
Stop the image
```
docker stop $(docker ps | grep "cdi-bundlea" | awk '{print $1}')
```

## cdi-blend-a
Pure Camel 3 CDI microservice that blend cdi-user and cdi-address reusing the original components to create a new service "usraddr".

```
...
INFO  Route: route1 started and consuming from: http://0.0.0.0:8080/api-doc
INFO  Route: route2 started and consuming from: http://0.0.0.0:8080/usraddr/findAll
INFO  Route: route3 started and consuming from: direct://myAddressService
INFO  Route: route4 started and consuming from: http://0.0.0.0:8080/address/findAll
INFO  Route: route5 started and consuming from: http://0.0.0.0:8080/user/findAll
...
```

### Building and running the image
Go to the root of your java project and run
```
mvn clean package docker:build
```
Runing the image
```
docker run -d -p 8080:8080 cdi-blenda:1.0-SNAPSHOT
```
Watch logs
```
docker logs $(docker ps | grep "cdi-blenda" | awk '{print $1}') -f
````
Stop the image
```
docker stop $(docker ps | grep "cdi-blenda" | awk '{print $1}')
```

## cdi-rabbit-producer
Pure Camel 3 CDI microservice that expose a HTTP endpoint and store messages in RabbitMQ.


## cdi-rabbit-consumer
Pure Camel 3 CDI microservice that read messages from RabbitMQ.

## cdi-rabbitmq kubernetes+istio
[Deployment scripts to deploy cdi-rabbit-producer, cdi-rabbit-consumer, upstream and rabbitmq in kubernetes+istio.](https://github.com/Emmerson-Miranda/camel/tree/master/camel3-cdi/istio07-camel-cdi-rabbitmq)


# The Dependency Inyection
Camel says:

```
The Camel CDI component provides auto-configuration for Apache Camel using CDI as dependency injection framework based on convention-over-configuration. 
It auto-detects Camel routes available in the application
```

So the four projects are demonstrating this principle works even if you add a library in your classpath with camel elements.

The only condition to convert each project as self-executable jar file is provide a main class like this.

````
import org.apache.camel.cdi.Main;

public class BlendAMainApp {
	
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
        main.close();
    }
    
}
````

# The Maven configuration
I assume you already are an expert in Maven, so enjoy the pom.xml :-)
