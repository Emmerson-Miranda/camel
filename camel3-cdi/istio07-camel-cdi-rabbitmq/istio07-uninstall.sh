kubectl delete --ignore-not-found=true -f istio07-03-virtualservice.yaml

kubectl delete --ignore-not-found=true -f istio07-02-gateway.yaml

kubectl delete --ignore-not-found=true -f istio07-01-cdi-deployments.yaml

echo "Sleeping 15secs to give k8s time to remove all containers!"
sleep 15