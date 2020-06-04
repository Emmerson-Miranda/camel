kubectl delete --ignore-not-found=true -f istio07-03-virtualservice.yaml

kubectl delete --ignore-not-found=true -f istio07-02-gateway.yaml


kubectl delete --ignore-not-found=true -f istio07-01-cdi-consumer.yaml
kubectl delete --ignore-not-found=true -f istio07-01-cdi-producer.yaml
kubectl delete --ignore-not-found=true -f istio07-01-mock-upstream.yaml
kubectl delete --ignore-not-found=true -f istio07-01-rabbitmq.yaml
