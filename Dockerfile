# Étape 1 : Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# On copie le fichier de configuration Maven
COPY pom.xml .

# On télécharge les dépendances (si ça échoue encore ici, le problème est dans ton pom.xml)
RUN mvn dependency:resolve -B

# On copie le code source
COPY src ./src

# Build du projet
RUN mvn clean package -DskipTests -B

# Étape 2 : Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# On utilise un wildcard pour attraper le jar, peu importe son nom
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]