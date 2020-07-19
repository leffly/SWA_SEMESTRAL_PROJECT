package cz.cvut.fel.swa.store.config;

import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import cz.cvut.fel.swa.store.service.CompleteOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CompleteOrderService completeOrderService(KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate, KafkaTopicConfig kafkaTopicConfig) {
        return new CompleteOrderService(kafkaTemplate, kafkaTopicConfig);
    }

}
