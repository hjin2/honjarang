package com.example.honjarang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {
        "com.example.honjarang.domain.jointdelivery.repository",
        "com.example.honjarang.domain.chat.repository"
})
@Configuration
public class MongoTemplateConfig {
}
