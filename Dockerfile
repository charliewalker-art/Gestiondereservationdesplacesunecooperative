# Étape 1 : Build avec exactement ta version de Java et Maven
FROM maven:3.9.13-eclipse-temurin-25 AS build
WORKDIR /app

# On copie le pom.xml
COPY pom.xml .

# On force Maven à ignorer les erreurs de transfert et on télécharge
RUN mvn dependency:go-offline -B || echo "Certaines dépendances seront téléchargées au build"

# On copie le code source
COPY src ./src

# Build du projet
# On ajoute -X pour voir l'erreur précise si ça plante encore
RUN mvn clean package -DskipTests -B

# Étape 2 : Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copie du jar (on utilise un pattern pour éviter les erreurs de nom)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]