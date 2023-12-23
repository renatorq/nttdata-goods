package com.nttdata.goods.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Arrays;
import java.util.List;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.topic.names}")
    private List<String> topicNames;

    @Bean
    public List<NewTopic> createNewTopics() {
        return Arrays.asList(
                TopicBuilder
                        .name(topicNames.get(0))
                        .build(),
                TopicBuilder
                        .name(topicNames.get(1))
                        .build()
        );
    }
}
