package cz.cvut.fel.swa.order.service;

import cz.cvut.fel.swa.order.model.Book;
import cz.cvut.fel.swa.order.model.Client;
import cz.cvut.fel.swa.order.model.Order;
import cz.cvut.fel.swa.order.repository.BookRepository;
import cz.cvut.fel.swa.order.repository.ClientRepository;
import cz.cvut.fel.swa.order.repository.OrderRepository;
import cz.cvut.fel.swa.order.repository.OrderedBooksRepository;
import cz.cvut.fel.swa.order.request.CompleteOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

public class OrderService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final OrderedBooksRepository orderedBooksRepository;

    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, ClientRepository clientRepository, OrderedBooksRepository orderedBooksRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.orderedBooksRepository = orderedBooksRepository;
    }

    public List<Order> findOrderByClientEmail(Client client) {
        List<Order> orders = orderRepository.findAllByClient_Email(client.getEmail());
        orders.forEach(o -> o.setClient(null));
        // TODO DTO
        return orders;
    }

    @KafkaListener(topics = "${calculateOrder}",
            groupId = "${bookGroupId}",
            containerFactory = "completeOrderRequestContainerFactory")
    public void listen(CompleteOrderRequest completeOrderRequest) {
        log.info("[CALCULATE_ORDER] Received Kafka Message: {}", completeOrderRequest);
        Client client = clientRepository.findByEmail(completeOrderRequest.getClient().getEmail());
        if (client == null) {
            client = completeOrderRequest.getClient();
            clientRepository.save(client);
        }

        List<Book> books = completeOrderRequest.getBooks();
        bookRepository.saveAll(books);

        // TODO STORE books

        Order order = new Order();
        order.setClient(client);
        int totalPrice = books.stream().mapToInt(Book::getPrice).sum();
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
    }
}
