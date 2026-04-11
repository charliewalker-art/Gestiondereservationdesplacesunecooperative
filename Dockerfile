# Étape 1 : Build (Construction)
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# On copie d'abord le pom.xml pour mettre en cache les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# On copie le reste du code source
COPY src ./src

# Compilation du projet
# On s'assure d'utiliser -B (batch mode) pour éviter les logs inutiles en CI
RUN mvn clean package -DskipTests -B

# Étape 2 : Runtime (Exécution)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Utilisation d'un argument pour ne pas dépendre du nom exact du fichier jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]