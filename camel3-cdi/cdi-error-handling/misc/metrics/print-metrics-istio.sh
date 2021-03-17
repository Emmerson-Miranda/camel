#!/bin/bash
#
# This print the prometheus metrics per pod and "k8s custom metrics" that prometheus adapter expose.
#
echo "Start"
echo "-----"

podnames=`kubectl get pods -l app=cdi-error-handling -o jsonpath --template='{range .items[*]}{.metadata.name}{"\n"}{end}'`

echo "Prometheus metrics from PODs:"
echo "----------------------------"
for item in $podnames
do

        count=`kubectl exec -it $item -c cdi-error-handling -- curl http://localhost:8888 | grep 'camel3_noerrorroute_count{journey="NoErrorRoute",propname="Count",type="timers",}' | awk '{ print $2 }'`
        count=$(awk '{print $1*$2}' <<<"${count} 1")
        
        oneminuterate=`kubectl exec -it $item -c cdi-error-handling -- curl http://localhost:8888 | grep 'camel3_noerrorroute_oneminuterate{journey="NoErrorRoute",propname="OneMinuteRate",type="timers",}' | awk '{ print $2 }'`
        oneminuterate2=$(awk '{print $1*$2}' <<<"${oneminuterate} 1")

        linea=""
        linea+="Pod: "
        linea+="$item "
        linea+="Count: "
        linea+="$count "
        linea+="OneMinuteRate: "
        linea+="$oneminuterate2 "

        echo "$linea"
done

echo "Prometheus-Adapter metrics:"
echo "----------------------------"

kubectl get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/istio_requests_total_rate"  | jq '.items[] | "Pod: " + .describedObject.name + " " + .metricName + " " + .value' |  sed "s/\"//g"

echo "-----"
kubectl get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/default/pods/*/istio_requests_total_sum_rate"  | jq '.items[] | "Pod: " + .describedObject.name + " " + .metricName + " " + .value' |  sed "s/\"//g"

echo "-----"
echo "End"
