#!/bin/bash

echo ""
echo "====================================================="
echo "*****************************************************"
echo ""
#generating prefix for all x-correlation-id calls
#reqIdPrefix=`env LC_CTYPE=C tr -dc "a-zA-Z0-9-_\$\?" < /dev/urandom | head -c 4`
reqIdPrefix=`uuidgen`
echo "x-correlation-id prefix $reqIdPrefix"

#setting total requests to send
maxRequests=320

#setting new POD configuration
kubectl apply -f istio07-test-scenario-01b.yaml

#forcing reload of pods with new configMaps
kubectl delete pod $(kubectl get pod -l app=consumer -n default -o jsonpath={.items..metadata.name})
kubectl delete pod $(kubectl get pod -l app=upstream -n default -o jsonpath={.items..metadata.name})

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
#Checking all requests in the log
echo "" > errors.txt
counter=0
while [  $counter -lt $maxRequests ]; do
   let counter=counter+1 
   echo "Checking request with x-correlation-id $reqIdPrefix-$counter"
   resultsCounter=`cat upstream.log | grep "\[$reqIdPrefix-$counter\]" | wc -l`
   echo "x-correlation-id $reqIdPrefix-$counter counter $resultsCounter"

   if [[ $resultsCounter -ne 1 ]]
   then
      echo "x-correlation-id $reqIdPrefix-$counter counter $resultsCounter"  >> errors.txt
      echo "" >> errors.txt
   fi

done


numErrors=$(cat errors.txt | grep "x-correlation-id" | wc -l)
echo ""
echo ""
echo "Total errors found $numErrors of $maxRequests"

#Test ended
echo ""
echo "*****************************************************"
echo "====================================================="
echo ""

exit $numErrors