package com.example.videostreamingapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class VideoStreamingApiApplicationTests {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	static {
		postgres.start();
		System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
		System.setProperty("spring.datasource.username", postgres.getUsername());
		System.setProperty("spring.datasource.password", postgres.getPassword());
	}

	@Test
	void contextLoads() {
		// If this test runs successfully, the context is loaded properly.
	}
}