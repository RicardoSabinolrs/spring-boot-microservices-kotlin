#!/bin/bash

usage(){
 cat << EOF

 Usage: $0 -f
 Description: To apply k8s manifests using the default \`kubectl apply -f\` command
[OR]
 Usage: $0 -k
 Description: To apply k8s manifests using the kustomize \`kubectl apply -k\` command
[OR]
 Usage: $0 -s
 Description: To apply k8s manifests using the skaffold binary \`skaffold run\` command

EOF
exit 0
}

logSummary() {
    echo ""
        echo "#####################################################"
        echo "Please find the below useful endpoints,"
        echo "Grafana - http://grafana.sabino-labs.192.168.99.100.nip.io"
        echo "#####################################################"
}

default() {
    suffix=k8s
    kubectl apply -f k8s/namespace/
    kubectl label namespace sabino-labs istio-injection=enabled --overwrite=true
    kubectl apply -f k8s/application/
    kubectl apply -f k8s/monitoring/prometheus-crd.yml
    until [ $(kubectl get crd prometheuses.monitoring.coreos.com 2>>/dev/null | wc -l) -ge 2 ]; do
        echo "Waiting for the custom resource prometheus operator to get initialised";
        sleep 5;
    done
    kubectl apply -f k8s/monitoring/prometheus-cr.yml
    kubectl apply -f k8s/monitoring/grafana.yml
    kubectl apply -f k8s/monitoring/grafana-dashboard.yml

}

kustomize() {
    kubectl apply -k ../
}

scaffold() {
    // this will build the source and apply the manifests the K8s target. To turn the working directory
    // into a CI/CD space, initilaize it with `skaffold dev`
    skaffold run
}

[[ "$@" =~ ^-[fks]{1}$ ]]  || usage;

while getopts ":fks" opt; do
    case ${opt} in
    f ) echo "Applying default \`kubectl apply -f\`"; default ;;
    k ) echo "Applying kustomize \`kubectl apply -k\`"; kustomize ;;
    s ) echo "Applying using skaffold \`skaffold run\`"; scaffold ;;
    \? | * ) usage ;;
    esac
done

logSummary
