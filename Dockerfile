# --- Etapa de construccion ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# --- Etapa de ejecucion ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY compras.csv compras.csv
EXPOSE 3001
ENTRYPOINT ["java", "-jar", "app.jar"]