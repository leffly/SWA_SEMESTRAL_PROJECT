package cz.cvut.fel.swa.book.config;

import cz.cvut.fel.swa.book.repository.BookRepository;
import cz.cvut.fel.swa.book.request.CompleteOrderRequest;
import cz.cvut.fel.swa.book.service.BookService;
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
    public BookService bookService(BookRepository bookRepository,
                                   KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate,
                                   KafkaTopicConfig kafkaTopicConfig) {
        return new BookService(bookRepository, kafkaTemplate, kafkaTopicConfig);
    }

}
