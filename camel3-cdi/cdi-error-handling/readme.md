# Camel 3 CDI - Error Handling


This example expose some REST interfaces and show some error handling scenarios


## Kubernetes deployment

 ```
$ kubectl apply -f kubernetes-deployment.yaml
```

If you want integrate your microservice with Prometheus please modify following annotations accordingly: 

 ```
  annotations:
    prometheus.io/scrape: "true"
    prometheus.io/path: "/"
    prometheus.io/port: "8888"
 ```
 

## Testing 

### Simple test using K8S service (Without istio)

```
kubectl get namespace -L istio-injection
kubectl label namespace default istio-injection=disabled --overwrite
kubectl apply -f ./misc/manifests/kubernetes-deployment.yaml  
kubectl apply -f ./misc/manifests/kubernetes-hpa.yaml  

minikube service cdi-error-handling-service --url

export PORT=<copy port from output of above command>
export HOST=$(minikube ip)
curl -v -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" http://$HOST:$PORT/eh/noerror
```

### Simple test using Istio (The Pod has injected Istio sidecar)

```
kubectl get namespace -L istio-injection
kubectl label namespace default istio-injection=enabled --overwrite
kubectl apply -f ./misc/manifests/kubernetes-deployment.yaml  
kubectl apply -f ./misc/manifests/kubernetes-istio.yaml  
kubectl apply -f ./misc/manifests/kubernetes-hpa.yaml  

minikube service istio-ingressgateway -n istio-system --url
export INGRESS_PORT=<copy port from output of above command>
export INGRESS_HOST=$(minikube ip)


curl -v -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -HHost:ceh.istio.com http://$INGRESS_HOST:$INGRESS_PORT/eh/noerror
```

### Load testing

Use JMeter project.


### Generic testing∫

NoErrorRoute Happy path.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/noerror
```

NoErrorRoute Happy path with Thread sleep.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -H "x-sleep: 3000" -X POST http://0.0.0.0:8080/eh/noerror
```

NumberRoute - Happy path -  without error handling.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/in
```

NumberRoute - Value NumberFormatException - NumberRoute without error handling.

```
$ curl -d "{\"value\": \"value with error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/in
```

InvalidNumberOnExceptionToRoute call a route that raise an exception, onException does not catch the error raise in the called route.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/inoeto
```

InvalidNumberRouteOnExceptionSelfContainedRoute has onException in the route, it catch the error.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/inoesc
```

InvalidNumberTryCatchRoute call another route that does not have onException but try catch block.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/intc
```

ErrorRoute always raise an exception.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/te
```

ErrorOnExceptionRoute Implementation Throw a Exception and is handled by onException

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/teoe
```

ErrorOnExceptionToRoute Call a route that throw an exception and handle it with caller onException, in this case onException is ignored.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/teoet
```

ErrorOnExceptionToTryCatchRoute Call a route that throw an exception and handle it with caller try/catch, in this case try/catch works."

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json"  -X POST http://0.0.0.0:8080/eh/teoetc
```


CallHttpBackendUndertowRoute Call backend HTTP with thread sleep - Bigger than timeout limit

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 3000"  -X POST http://0.0.0.0:8080/eh/chbu
```



CallHttpBackendUndertowRoute Call backend HTTP with thread sleep - Lower than timeout limit

```
curl -d "{\"value\": \"wwww1\"}" -H "Content-Type: application/json" -H "x-sleep: 1000"  -X POST http://0.0.0.0:8080/eh/chbu

```


CallHttpBackendHttpRoute Call backend HTTP with thread sleep - Happy path

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 1000"  -X POST http://0.0.0.0:8080/eh/chbh
```

CallHttpBackendHttpRoute Call backend HTTP with thread sleep - Timeout exception handled by second onException block.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 3000"  -X POST http://0.0.0.0:8080/eh/chbh
```

CallHttpBackendHttpRoute Call backend HTTP with an invalid number - The first onException block will catch the error.

```
curl -d "{\"value\": \"efdw1\"}" -H "Content-Type: application/json" -H "x-sleep: 1000"  -X POST http://0.0.0.0:8080/eh/chbh
```

### XML Validation Route

Validation is fine

```
curl -d "@src/main/resources/schemas/PublicationCatalogue/Catalogue.xml" -H "Content-Type: application/xml" -H "x-schema: PublicationCatalogue/Catalogue.xsd"  -X POST http://0.0.0.0:8080/eh/schema/xml
```

Validation fails because the schema given does not exist

```
curl -d "@src/main/resources/schemas/PublicationCatalogue/Catalogue.xml" -H "Content-Type: application/xml" -H "x-schema: unknown.xsd"  -X POST http://0.0.0.0:8080/eh/schema/xml
```

Validation fails because mandatory element is missing

```
curl -d "@src/main/resources/schemas/PublicationCatalogue/InvalidCatalogue.xml" -H "Content-Type: application/xml" -H "x-schema: PublicationCatalogue/Catalogue.xsd"  -X POST http://0.0.0.0:8080/eh/schema/xml
```

Validation fails because element does not exist in the schema

```
curl -d "@src/main/resources/schemas/PublicationCatalogue/CatalogueWithoutMandatory.xml" -H "Content-Type: application/xml" -H "x-schema: PublicationCatalogue/Catalogue.xsd"  -X POST http://0.0.0.0:8080/eh/schema/xml
```


### JSON Validation Route

Validation is fine

```
curl -d "@src/main/resources/schemas/myjsonschema/myjsonexample.json" -H "Content-Type: application/json" -H "x-schema: myjsonschema/myjsonschema.json"  -X POST http://0.0.0.0:8080/eh/schema/json
```

Missing required attributes

```
curl -d "@src/main/resources/schemas/myjsonschema/myjsonexampleWithoutRequired.json" -H "Content-Type: application/json" -H "x-schema: myjsonschema/myjsonschema.json"  -X POST http://0.0.0.0:8080/eh/schema/json
```

Schema does not exist

```
curl -d "@src/main/resources/schemas/myjsonschema/myjsonexample.json" -H "Content-Type: application/json" -H "x-schema: unknown.json"  -X POST http://0.0.0.0:8080/eh/schema/json
```
