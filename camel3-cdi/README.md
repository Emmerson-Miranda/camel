# Introduction
This folder contains some demos to demostrate how to create Camel 3 microservices using Camel CDI (avoiding Spring Boot like applications).

All examples provide OpenAPI (swagger) specification and use "undertow" as a web server.

# The microservices

## cdi-user
Pure Camel 3 CDI microservice tha provides dummy user information.

## cdi-address
Pure Camel 3 CDI microservice tha provides dummy address information.

## cdi-bundle-a
Pure Camel 3 CDI microservice that bundle cdi-user and cdi-address reusing the original components.

## cdi-blend-a
Pure Camel 3 CDI microservice that blend cdi-user and cdi-address reusing the original components to create a new service "usraddr".

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
