package com.example.honjarang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories(basePackages = "com.example.honjarang.domain.jointdelivery.repository")
@Configuration
public class MongoTemplateConfig {
}
