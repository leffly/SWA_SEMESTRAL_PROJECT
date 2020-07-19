package cz.cvut.fel.swa.order.config;

import cz.cvut.fel.swa.order.repository.BookRepository;
import cz.cvut.fel.swa.order.repository.ClientRepository;
import cz.cvut.fel.swa.order.repository.OrderRepository;
import cz.cvut.fel.swa.order.repository.OrderedBooksRepository;
import cz.cvut.fel.swa.order.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     BookRepository bookRepository,
                                     ClientRepository clientRepository,
                                     OrderedBooksRepository orderedBooksRepository) {
        return new OrderService(orderRepository, bookRepository, clientRepository, orderedBooksRepository);
    }

}
