pipeline {
    agent any

    environment {
        GCP_PROJECT = 'restapispringboot' // Substitua pelo ID do projeto GCP
        GKE_CLUSTER = 'rest-api-cluster-1' // Nome do cluster GKE
        GKE_ZONE = 'us-central1-a' // Ex.: us-central1-a
        IMAGE_NAME = 'rest-api-spring' // Nome da imagem Docker
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        REGION = 'us-central1' // Define the region for Artifact Registry
        REPOSITORY_NAME = 'docker-repository'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/lisbao1303/rest-api-spring.git' // Repositório
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh './mvnw clean test' // Executa os testes com Maven Wrapper
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    sh './mvnw clean package -P release -DskipTests' // Realiza o build da aplicação
                }
            }
        }

        stage('Prepare Environment') {
            steps {
                // Carregar o arquivo secreto
                withCredentials([file(credentialsId: 'my-env-file', variable: 'ENV_FILE')]) {
                    // Copiar o arquivo .env para o workspace
                    sh 'cp $ENV_FILE .env'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        docker build -f Dockerfile -t $REGION-docker.pkg.dev/$GCP_PROJECT/$REPOSITORY_NAME/$IMAGE_NAME:latest .
                    """
                }
            }
        }

        stage('Authenticate with Google Cloud') {
            steps {
                script {
                    // Usando a credencial da conta de serviço do Google Cloud
                    withCredentials([file(credentialsId: 'my-google-cloud-service-file', variable: 'GOOGLE_CREDENTIALS_JSON')]) {
                        // Ativar as credenciais da conta de serviço uma vez no início do pipeline
                        sh """
                            export GOOGLE_APPLICATION_CREDENTIALS=\$GOOGLE_CREDENTIALS_JSON
                            gcloud auth activate-service-account --key-file=\$GOOGLE_APPLICATION_CREDENTIALS
                            gcloud config set project $GCP_PROJECT
                        """
                    }
                }
            }
        }

        stage('Push Docker Image to GCR') {
            steps {
                script {
                    sh """
                         gcloud auth configure-docker $REGION-docker.pkg.dev
                         docker push $REGION-docker.pkg.dev/$GCP_PROJECT/$REPOSITORY_NAME/$IMAGE_NAME:latest
                    """
                }
            }
        }

        stage('Validate Docker Compose') {
            steps {
                script {
                    // Verifica se o docker-compose.yml está correto
                    sh """
                        docker-compose -f $DOCKER_COMPOSE_FILE config
                    """
                }
            }
        }

        stage('Kompose Convert') {
            steps {
                script {
                    // Converte o docker-compose.yml para manifests do Kubernetes
                    sh """
                        kompose convert --volumes persistentVolumeClaim --out ./k8s/
                    """
                }
            }
        }

        stage('Deploy to GKE') {
            steps {
                script {
                    // Configura o acesso ao cluster GKE
                    sh """
                        gcloud container clusters get-credentials $GKE_CLUSTER --zone $GKE_ZONE --project $GCP_PROJECT
                    """

                    // Aplica os manifests convertidos no cluster
                    sh """
                        kubectl apply -f ./k8s/
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline concluído.'
        }
        success {
            echo 'Deploy realizado com sucesso no GKE!'
        }
        failure {
            echo 'O pipeline falhou. Verifique os logs.'
        }
    }
}
