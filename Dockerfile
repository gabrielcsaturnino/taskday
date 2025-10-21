# Multi-stage build para otimização
FROM maven:3.9.9-openjdk-17-slim AS builder

WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cache layer)
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Criar usuário não-root para segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Instalar curl para health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copiar JAR do build stage
COPY --from=builder /app/target/*.jar app.jar

# Configurar permissões
RUN chown -R appuser:appuser /app
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]