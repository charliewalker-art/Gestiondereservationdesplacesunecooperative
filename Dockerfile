# Étape 1 : Build (Construction)
FROM maven:3.9.13-eclipse-temurin-25-alpine AS build
WORKDIR /app

# On copie le pom.xml et on télécharge les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# On copie le code source et on compile le .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Runtime (Exécution)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copie du jar compilé
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]