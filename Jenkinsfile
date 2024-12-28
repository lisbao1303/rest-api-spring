pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
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

        stage('Build and Run Docker Compose') {
            steps {
                script {
                    sh '''
                    docker compose down
                    docker compose build
                    docker compose up -d
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline concluído.'
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'O pipeline falhou. Verifique os logs.'
        }
    }
}
