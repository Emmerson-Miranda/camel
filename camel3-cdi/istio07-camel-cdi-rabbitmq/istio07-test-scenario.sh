#!/bin/bash

echo ""
echo "====================================================="
echo "*****************************************************"
echo ""
#generating prefix for all x-correlation-id calls
reqIdPrefix=`uuidgen`
echo "x-correlation-id prefix $reqIdPrefix"

#setting total requests to send
maxRequests=700

#setting consumer replicas
consumerReplicas=3

#setting new POD configuration
kubectl apply -f istio07-test-scenario-03b.yaml

#forcing number of replicas
kubectl scale --replicas=$consumerReplicas deployment/consumer

#forcing reload of pods with new configMaps
kubectl delete pod $(kubectl get pod -l app=upstream -n default -o jsonpath={.items..metadata.name})
kubectl delete pod $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name})


export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export INGRESS_HOST=$(minikube ip)

#Sending requests
counter=0
while [  $counter -lt $maxRequests ]; do
   let counter=counter+1 
   echo "Sending request with x-correlation-id $reqIdPrefix-$counter"
   curl -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/service -d "{\"ok\": \"value without error\"}" -H "Content-Type: application/json" -H "X-Correlation-ID: $reqIdPrefix-$counter" -H "test-scenario: ok" -H "X-US-SCENARIO: 200" 
   echo ""
done

curl -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/producer/prometheus > producer_prometheus.log

echo "-----------------------------------------------------"
#Waiting for full logs
while true; do
   echo "Checking logs - Waiting 10 sec."
   sleep 10
   echo "Checking logs - Downloading logs from upstream"
   kubectl logs $(kubectl get pod -l app=upstream -n default -o jsonpath={.items..metadata.name}) -c upstream > upstream.log
   resultsCounter=`cat upstream.log | grep "\[$reqIdPrefix-$counter\]" | wc -l`  
   if [[ $resultsCounter -gt 0 ]]
   then
      echo "Checking logs - Last request found, logs are completed ($resultsCounter)."
      break
   fi
done

echo "-----------------------------------------------------"
echo "Downloading pods logs"
pods=$(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name})
echo "" > consumer_log_files.log
rm consumer_log_files.log
IFS=' ' # space is set as delimiter
read -ra ADDR <<< "$pods" # pods is read into an array as tokens separated by IFS
for i in "${ADDR[@]}"; do # access each element of array
    echo "$i-console.log" >> consumer_log_files.log
    echo "$i" > "$i-console.log"
    kubectl logs $i -c cdi-rabbit-consumer >> "$i-console.log"
    echo "------------------------------------------------"  >> "$i-console.log"
    echo "" >> "$i-console.log"
done

echo "-----------------------------------------------------"
#Checking all requests in the log
echo "" > errors.txt
counter=0
while [  $counter -lt $maxRequests ]; do
   let counter=counter+1 
   echo "Checking request with x-correlation-id $reqIdPrefix-$counter"
   resultsCounter=`cat upstream.log | grep "x-correlation-id: \[$reqIdPrefix-$counter\]" | wc -l`
   echo "x-correlation-id $reqIdPrefix-$counter counter $resultsCounter"

   if [[ $resultsCounter -ne 1 ]]
   then
      echo "x-correlation-id $reqIdPrefix-$counter counter $resultsCounter"  >> errors.txt
      echo "" >> errors.txt
      #search error in each pod log
      textToFind="$reqIdPrefix-$counter"
      input=consumer_log_files.log
      while IFS= read -r line
      do
         echo "$line"
         grep $textToFind "$line"
      done < "$input"

   fi
done

curl -HHost:poc07.istio.com http://$INGRESS_HOST:$INGRESS_PORT/consumer/prometheus > consumer_prometheus.log


numErrors=$(cat errors.txt | grep "x-correlation-id" | wc -l)
echo ""
echo ""
echo "Total errors found $numErrors of $maxRequests"

kubectl exec $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name}) -c cdi-rabbit-consumer  -- cat /var/log/onException.log  > onException.log

mkdir -p logs
cp upstream.log "logs/log-$reqIdPrefix-upstream.log"
cp onException.log "logs/log-$reqIdPrefix-onException.log"
cp errors.txt "logs/log-$reqIdPrefix-errors.txt"
cp producer_prometheus.log "logs/log-$reqIdPrefix-producer_prometheus.txt"
cp consumer_prometheus.log "logs/log-$reqIdPrefix-consumer_prometheus.txt"


#Test ended
echo ""
echo "*****************************************************"
echo "====================================================="
echo ""

exit $numErrors