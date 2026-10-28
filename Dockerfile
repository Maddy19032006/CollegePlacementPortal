# ---------- Stage 1: build the jar with Maven ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom first so dependency download is cached by Docker layer cache
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
# Run tests during the image build; the build fails if any test fails
RUN mvn -B clean package

# ---------- Stage 2: minimal runtime image ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /app/target/placement-portal*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
