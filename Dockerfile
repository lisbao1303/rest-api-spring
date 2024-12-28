# Base image apenas para execução
FROM openjdk:17-jdk-slim

# Definir variáveis de ambiente
ENV PROJECT_HOME /usr/src/rest-api-spring
ENV JAR_NAME rest-api-spring.jar

# Criar o diretório do projeto
WORKDIR $PROJECT_HOME

# Copiar o JAR previamente gerado para o contêiner
COPY target/$JAR_NAME $PROJECT_HOME/

# Configurar o comando de inicialização
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "rest-api-spring.jar"]
