Camel - Spring boot - Microservice
===
This is a POC to show the basics about how to create a REST microservice using Apache Camel and Spring Boot.

As part of this POC the maven configuration will generate a Docker image packaging the microservice.

Package solution
---
```
$ mvn clean package docker:build
```

After build we can check the docker image created.

```
$ docker ps
CONTAINER ID        IMAGE                                COMMAND                  CREATED             STATUS              PORTS                    NAMES
1275665986d3        helloworld-camel-spring-boot:0.0.2   "java -jar /hellow..."   About an hour ago   Up About an hour    0.0.0.0:8080->8080/tcp   pensive_babbage
[developer@localhost helloworld-camel-spring-boot]$ 
```


Starting the container
---
```
$ docker run -d -p 8080:8080 helloworld-camel-spring-boot:0.0.2 
```

*URLs to call*

After start the container you can test the microservice calling one of the following addresses (wait a few seconds before call them):
* http://localhost:8080/camel/say/hello/
* http://localhost:8080/camel/say/hello/someName
* http://localhost:8080/camel/say/helloObject/someName
* http://localhost:8080/camel/say/greetings/someName

Access to the container
---
Its possible access to the container console using following command.

```
$ docker run -it - -entrypoint /bin/bash helloworld-camel-spring-boot:0.0.2
```


Miscellaneous
---
Maven docker plugin documentation at https://github.com/spotify/docker-maven-plugin

