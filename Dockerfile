# ========================================
# Multi-stage Dockerfile para CityTransit Backend
# Java 21 + Spring Boot 3.2.0 + ML/DL
# ========================================

# ==================== STAGE 1: Build ====================
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copiar pom.xml primero (para cache de dependencias)
COPY pom.xml ./

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B || true

# Copiar código fuente
COPY src ./src

# Compilar aplicación (sin tests para ser más rápido)
RUN mvn clean package -DskipTests -B

# ==================== STAGE 2: Runtime ====================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Crear usuario no-root por seguridad
RUN groupadd -r spring && useradd -r -g spring spring

# Copiar el JAR compilado desde stage anterior
COPY --from=build /app/target/citytransit-backend-*.jar app.jar

# Cambiar permisos
RUN chown -R spring:spring /app

# Cambiar a usuario no-root
USER spring:spring

# Exponer puerto de la aplicación
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker \
    JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC" \
    TZ=America/La_Paz

# Ejecutar la aplicación con optimizaciones
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
