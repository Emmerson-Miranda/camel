#https://istio.io/docs/tasks/traffic-management/ingress/ingress-control/

echo "Default namespace will not inject automatically the istio sidecar"
kubectl get namespace -L istio-injection
kubectl label namespace default istio-injection=enabled
kubectl get namespace -L istio-injection

echo "--- Deploying applications"
kubectl apply -f istio07-01-cdi-deployments.yaml

echo "--- Creating istio gateway"
kubectl apply -f istio07-02-gateway.yaml

echo "--- Creating istio virtualservice"
kubectl apply -f istio07-03-virtualservice.yaml

export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export INGRESS_HOST=$(minikube ip)

sleep 10
echo ""
echo ""
echo "------------------------------------------------------------------"
echo "--- Valid request - producer prometheus metrics"
echo "------------------------------------------------------------------"
echo "curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/prometheus"
curl -v -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/prometheus


#watch -n 1 curl -HHost:poc07.istio.com -o /dev/null -s -w %{http_code} http://$INGRESS_HOST:$INGRESS_PORT/producer/prometheus

#change envoy sidecar logs
#kubectl exec -it $(kubectl get pod -l app=producer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- /bin/bash
#curl localhost:15000/logging?level=debug -X POST

#access to localhost to the cluster
#prometheus
#kubectl port-forward $(kubectl get pod -l app=producer -n default -o jsonpath={.items..metadata.name}) 7000:8888
#then curl http://localhost:7000

#summary of clusters, listeners or routes
#istioctl proxy-config cluster -n istio-system $(kubectl get pod -l app=istio-ingressgateway  -n istio-system -o jsonpath={.items..metadata.name})
