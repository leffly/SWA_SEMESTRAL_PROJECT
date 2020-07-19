package cz.cvut.fel.swa.book.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${bookifyMessagingUrl}")
    private String bootstrapAddress;

    @Value(value = "${bookOfferTopic}")
    private String bookOfferTopic;

    @Value(value = "${complementOrderTopic}")
    private String complementOrderTopic;

    @Value(value = "${calculateOrder}")
    private String calculateOrderTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic bookOffer() {
        return new NewTopic(bookOfferTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic complementOrder() {
        return new NewTopic(complementOrderTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic calculateOrder() {
        return new NewTopic(calculateOrderTopic, 1, (short) 1);
    }

}
