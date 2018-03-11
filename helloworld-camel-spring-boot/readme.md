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
Maven docker plugin documentation at [https://github.com/spotify/docker-maven-plugin](https://github.com/spotify/docker-maven-plugin)


Pushing image to Docker Hub
---
Now is time to push the image in Docker Hub, full documentation in [https://ropenscilabs.github.io/r-docker-tutorial/04-Dockerhub.html](https://ropenscilabs.github.io/r-docker-tutorial/04-Dockerhub.html)

```
[developer@localhost ~]$ docker login --username=emmerson 
Password: 
Login Succeeded
[developer@localhost ~]$ docker images
REPOSITORY                     TAG                 IMAGE ID            CREATED             SIZE
helloworld-camel-spring-boot   0.0.2               6939011ece3f        12 days ago         678MB
helloworld-camel-spring-boot   latest              6939011ece3f        12 days ago         678MB
<none>                         <none>              bc9bde99b5f5        12 days ago         678MB
<none>                         <none>              245cdec8ae09        12 days ago         678MB
java                           8                   d23bdf5b1b1b        13 months ago       643MB
[developer@localhost ~]$ docker tag 6939011ece3f emmerson/helloworld-camel-spring-boot:0.0.2
[developer@localhost ~]$ docker push emmerson/helloworld-camel-spring-boot:0.0.2
The push refers to a repository [docker.io/emmerson/helloworld-camel-spring-boot]
0318b110d00d: Pushed 
35c20f26d188: Mounted from library/java 
c3fe59dd9556: Mounted from library/java 
6ed1a81ba5b6: Mounted from library/java 
a3483ce177ce: Mounted from library/java 
ce6c8756685b: Mounted from library/java 
30339f20ced0: Mounted from library/java 
0eb22bfb707d: Mounted from library/java 
a2ae92ffcd29: Mounted from library/java 
0.0.2: digest: sha256:2d23f014924acee799d63ff72ac7b57b8a1d143dba36f78f9dc31913ffbf42f5 size: 2212
[developer@localhost ~]$ 

```

So now the image is available from Docker Hub [https://hub.docker.com/r/emmerson/helloworld-camel-spring-boot/](https://hub.docker.com/r/emmerson/helloworld-camel-spring-boot/)
