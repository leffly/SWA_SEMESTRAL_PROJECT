package cz.cvut.fel.swa.order.service;

import cz.cvut.fel.swa.order.config.KafkaTopicConfig;
import cz.cvut.fel.swa.order.model.Book;
import cz.cvut.fel.swa.order.model.Client;
import cz.cvut.fel.swa.order.model.Order;
import cz.cvut.fel.swa.order.repository.BookRepository;
import cz.cvut.fel.swa.order.repository.ClientRepository;
import cz.cvut.fel.swa.order.repository.OrderRepository;
import cz.cvut.fel.swa.order.repository.OrderedBooksRepository;
import cz.cvut.fel.swa.order.request.CompleteOrderRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final KafkaTopicConfig kafkaTopicConfig;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final OrderedBooksRepository orderedBooksRepository;

    public OrderService(OrderRepository orderRepository, KafkaTopicConfig kafkaTopicConfig, BookRepository bookRepository, ClientRepository clientRepository, OrderedBooksRepository orderedBooksRepository) {
        this.orderRepository = orderRepository;
        this.kafkaTopicConfig = kafkaTopicConfig;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.orderedBooksRepository = orderedBooksRepository;
    }

    public List<Order> findOrderByClientEmail(Client client){
        List<Order> orders = orderRepository.findAllByClient_Email(client.getEmail());
        orders.forEach(o->{
            o.setClient(null);
        });
        // TODO DTO
        return orders;
    }

    @KafkaListener(topics = "${calculateOrder}",
            groupId = "${bookGroupId}",
            containerFactory = "completeOrderRequestContainerFactory")
    public void listen(CompleteOrderRequest completeOrderRequest) {
        LOGGER.log(Level.INFO,"[CALCULATE_ORDER] Received Kafka Messasge: " + completeOrderRequest.toString());
        Client client = clientRepository.findByEmail(completeOrderRequest.getClient().getEmail());
        if(client == null){
            client = completeOrderRequest.getClient();
            clientRepository.save(client);
        }

        List<Book> books = completeOrderRequest.getBooks();
        bookRepository.saveAll(books);

        //TODO STORE books

        Order order = new Order();
        order.setClient(client);
        AtomicReference<Integer> totalPrice = new AtomicReference<>(0);
        books.forEach(b->{
            totalPrice.updateAndGet(v -> v + b.getPrice());
        });
        order.setTotalPrice(totalPrice.get());

        orderRepository.save(order);
    }
}
