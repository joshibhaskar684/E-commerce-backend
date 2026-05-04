package app.auth.service.KafkaConfiguration;

import app.auth.service.Producers.KafkaTopicProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    private final KafkaTopicProperties topics;

    public KafkaTopicConfiguration(KafkaTopicProperties topics) {
        this.topics = topics;
    }

    @Bean
    public NewTopic sellerTopic() {
        return TopicBuilder.name(topics.getSeller())
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cartTopic() {
        return TopicBuilder.name(topics.getCart())
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic shopTopic() {
        return TopicBuilder.name(topics.getShop())
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name(topics.getOrder())
                .partitions(3)
                .replicas(1)
                .build();
    }
}