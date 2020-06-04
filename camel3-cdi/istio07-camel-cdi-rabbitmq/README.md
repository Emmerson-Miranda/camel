# K8S Deployment (Camel CDI 3.3 with RabbitMQ)

This POC deploy following containers:

- RabbitMQ broker
- Upstream/backend server (based on wiremock)
- Publish/Produce messages into RMQ
- Consume messages from RMQ and send message to upstream server

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

curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/upstream/service -d "body" -H "X-US-SCENARIO: 200" -v
```

