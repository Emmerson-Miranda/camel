kubectl exec $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/reset_counters -X POST 
kubectl exec $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/reset_counters -X POST 
kubectl exec $(kubectl get pod -l app=rabbitmq -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/reset_counters -X POST 
kubectl exec $(kubectl get pod -l app=upstream -n default -o jsonpath={.items..metadata.name}) -c istio-proxy -- curl localhost:15000/reset_counters -X POST 
