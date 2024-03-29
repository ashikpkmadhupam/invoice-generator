FROM maven:3.8.3-openjdk-17 as builder
WORKDIR /app
COPY . .
RUN mvn package


FROM openjdk:17.0.2-oracle
WORKDIR /app
EXPOSE 8080
RUN mkdir -p src/main/resources/images
COPY --from=builder /app/target/invoice-generator-0.0.1.jar .
COPY --from=builder /app/ .
CMD java -jar invoice-generator-0.0.1.jar
