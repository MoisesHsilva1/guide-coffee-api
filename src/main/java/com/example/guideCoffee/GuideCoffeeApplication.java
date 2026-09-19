package com.example.guideCoffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class GuideCoffeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuideCoffeeApplication.class, args);
	}

}
