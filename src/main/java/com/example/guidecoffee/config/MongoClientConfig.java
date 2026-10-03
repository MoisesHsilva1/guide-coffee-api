package com.example.guidecoffee.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class MongoClientConfig {

    @Bean(destroyMethod = "close")
    public MongoClient mongoClient(@Value("${spring.data.mongodb.uri}") String mongoUri) {
        if (!StringUtils.hasText(mongoUri)) {
            throw new IllegalStateException("spring.data.mongodb.uri não foi configurada");
        }

        return MongoClients.create(new ConnectionString(mongoUri));
    }
}
