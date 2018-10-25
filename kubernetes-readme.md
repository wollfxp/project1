# Deploying application to Kubernetes

1. Converting your docker-compose file to kubernetes format: `kompose convert -f docker-compose.yaml`
2. deploy them to kubernetes `kubectl apply -f .`
3. check the pods' status `kubectl get pods`
4. find out what is the name of app pod: `kubectl get pods -l io.kompose.service=starships -o jsonpath="{.items[0].metadata.name}"`
5. port-forward the application port to local machine `kubectl port-forward <POD_NAME> 8443:8443`
6. access [https://localhost:8443/]
7. it works!