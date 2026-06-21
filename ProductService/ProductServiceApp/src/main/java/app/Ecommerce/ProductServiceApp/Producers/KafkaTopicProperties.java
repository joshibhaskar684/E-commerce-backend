package app.Ecommerce.ProductServiceApp.Producers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.topics")
public class KafkaTopicProperties {
    private String products;
    private String seller;
    private String shop;
    private String cart;
    private String order;
    private String admin;

}