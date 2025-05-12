# Etapa 1: Usar uma imagem base com o JDK
FROM openjdk:21-jdk-slim as builder

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo .jar para o diretório de trabalho
COPY target/GerenciamentoDelivery-0.0.1-SNAPSHOT.jar app.jar

# Etapa 2: Usar uma imagem base do JDK para rodar a aplicação
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo .jar do build para a imagem final
COPY --from=builder /app/app.jar /app/app.jar

# Expor a porta 8082
EXPOSE 8082

# Comando para executar o arquivo .jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
