
package com.techy.microservices.product;

		import org.springframework.boot.test.context.TestConfiguration;
		import org.springframework.context.annotation.Bean;
		import org.testcontainers.containers.MongoDBContainer;
		import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainersConfig {

	private static final MongoDBContainer mongoDBContainer =
			new MongoDBContainer(DockerImageName.parse("mongo:7.0.5"));

	static {
		mongoDBContainer.start();
	}

	@Bean
	public static MongoDBContainer mongoDBContainer() {
		return mongoDBContainer;
	}
}

