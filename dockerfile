FROM openjdk:17
WORKDIR /app
COPY MotorcycleShop-db-test/target/MotorcycleShop-db-test-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "MotorcycleShop-db-test-0.0.1-SNAPSHOT.jar"]
