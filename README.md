# movies-app

### set up docker composer
```shell
git clone https://github.com/musimarcin/movies-api.git
./compose.sh start   #to start containers for dp and api
./compose.sh stop    #to stop all containers
./compose.sh start_db  #to start db container
./compose.sh stop_db   #stop db container
```

### set up kubernetes
```shell
cd kubernetes/kind
./create-cluster.sh # creates kind cluster
cd ..
kubectl apply -f . # initialize pods, pv, pvc, services and ingress
```

API will be availe through NodePort: http://localhost:18080/api/movies  
with front-end: http://localhost:30030/  
API using Ingress: http://localhost/movies-api/api/movies  
front-end: http://localhost/  
###### TODO: fix imagepull for front-end version

## Tech Stack:  
Languages: Java, TypeScript  
Frameworks: Spring Boot, React, Node.js, Next.js, Boostrap  
Database: PostgreSQL, H2  
Dev: Git, Docker, Kubernetes


