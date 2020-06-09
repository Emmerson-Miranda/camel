# K8S Deployment (Camel CDI 3.3 with RabbitMQ)

This POC deploy following containers:

- RabbitMQ broker
- Upstream/backend server (based on wiremock)
- Publish/Produce messages into RMQ
- Consume messages from RMQ and send message to upstream server

## Environment
Configuration
```
$ minikube config view
- ingress-dns: false
- memory: 10240
- container-runtime: docker
- cpus: 8
- dashboard: true
- driver: hyperkit
```

```
$ minikube start
😄  minikube v1.11.0 on Darwin 10.15.5
✨  Using the hyperkit driver based on existing profile
👍  Starting control plane node minikube in cluster minikube
🔄  Restarting existing hyperkit VM for "minikube" ...
🐳  Preparing Kubernetes v1.18.3 on Docker 19.03.8 ...
```

## Install
Run istio07-install.sh

## Unistall
Run istio07-uninstall.sh

## Testing

Preparation

```
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export INGRESS_HOST=$(minikube ip)
````

Curl commands to test

```
curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/prometheus

curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/consumer/prometheus  | grep "amqp_consumer" | more

curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/upstream/service -d "body" -H "X-US-SCENARIO: 200" 

curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/service -d "{\"ok\": \"value without error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ok" -H "X-US-SCENARIO: 200" 

curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/service -d "{\"ok\": \"value with error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: myCustomXCID5" -H "test-scenario: ok" -H "X-US-SCENARIO: 500" 
```

## Utility commands

Access RabbitMQ
```
kubectl port-forward $(kubectl get pod -l app=rabbitmq -n default -o jsonpath={.items..metadata.name}) 15672:15672
```
open browser at http://localhost:15672


Kill envoy-proxy (to test failure recovery)
```
kubectl exec $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/quitquitquit -X POST
kubectl exec $(kubectl get pod -l app=rabbitmq -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/quitquitquit -X POST
```

Changing istio-proxy logging level (levels: trace debug info warning error critical off)

https://www.envoyproxy.io/docs/envoy/latest/operations/admin
```
kubectl exec $(kubectl get pod -l app=producer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/logging?level=debug -X POST
kubectl exec $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/logging?level=debug -X POST
kubectl exec $(kubectl get pod -l app=rabbitmq -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/logging?level=debug -X POST
```

Monitoring logs

```
kubectl logs -f $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c cdi-rabbit-consumer
kubectl logs -f $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy

kubectl logs -f $(kubectl get pod -l app=rabbitmq -n default -o jsonpath={.items..metadata.name}) -c rabbitmq
kubectl logs -f $(kubectl get pod -l app=rabbitmq -n default -o jsonpath={.items..metadata.name}) -c istio-proxy
```
