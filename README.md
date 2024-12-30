REST API SpringBoot Example 
- JWT Auth ("/user" -> create new User, "/login" -> get jwt Bearer token return on header)
- docker-compose.yml services: MysqlDB and rest-api-spring image FROM openjdk:17-jdk-slim
- Pipeline Jenkins to execute tests, package jar file, build docker image and deploy the pods in GKE after authentication with google-cloud-service-account json key.

![Jenkins Pipeline](images/jenkins.png)

DEPENDENCIES FOR JENKINS:
The Jenkins Server needs Maven, Docker plugins and access in terminal:
- `kompose` [Kompose instalations](https://kompose.io/installation/)
- `docker compose` [Docker Desktop Instalation](https://docs.docker.com/desktop/)
- `gcloud` [Google Cloud CLI Instalation](https://cloud.google.com/sdk/docs/install?hl=pt-br)
- `kubectl` [Kubernates Instalation](https://kubernetes.io/docs/tasks/tools/)

- YOU CAN SET A WEEBHOOK IN GITHUB AND CONFIGURE A JENKINS PIPELINE 

LOCAL RUN:
Running the Application (Classical Maven/SpringBoot Application)
- Install Dependencies:
`mvn clean install`
- Using Maven:
`mvn spring-boot:run`
- Building the Project:
`mvn clean package`
- Run the jar file:
`java -jar target/app-name-0.0.1-SNAPSHOT.jar`

Using Docker Compose:

- `docker build -f Dockerfile -t $REGION-docker.pkg.dev/$GCP_PROJECT/$REPOSITORY_NAME/$IMAGE_NAME:latest .` (! .env or change image path on docker-compose.yml)
- `docker-compose up -d`

