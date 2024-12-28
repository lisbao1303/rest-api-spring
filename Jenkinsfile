pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/lisbao1303/rest-api-spring.git' // repositório
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

        stage('Build and Run Docker Compose') {
            steps {
                script {
                    sh '''
                    docker-compose down
                    docker-compose build
                    docker-compose up -d
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
