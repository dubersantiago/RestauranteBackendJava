# =========================
# ETAPA 1: BUILD
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# =========================
# ETAPA 2: RUNTIME
# =========================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Puerto
EXPOSE 8080

# Ejecutar app con variables
ENTRYPOINT ["java","-jar","/app/app.jar"]
