package com.example.motorcycleshop.container;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class ContainerConfig {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14.1")
                    .withDatabaseName("postgres")
                    .withPassword("root")
                    .withUsername("postgres");

@DynamicPropertySource
    public static void containerConfig(DynamicPropertyRegistry dynamicPropertyRegistry){
    dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

}
