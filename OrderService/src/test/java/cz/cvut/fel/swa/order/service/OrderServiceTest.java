package cz.cvut.fel.swa.order.service;

import cz.cvut.fel.swa.order.enums.BookCarrierType;
import cz.cvut.fel.swa.order.model.Book;
import cz.cvut.fel.swa.order.model.Client;
import cz.cvut.fel.swa.order.repository.BookRepository;
import cz.cvut.fel.swa.order.repository.ClientRepository;
import cz.cvut.fel.swa.order.repository.OrderRepository;
import cz.cvut.fel.swa.order.request.CompleteOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@EmbeddedKafka(partitions = 1, topics = {"testTopic"})
@SpringBootTest
public class OrderServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    OrderService orderService;

    Client client;
    CompleteOrderRequest completeOrderRequest;

    @BeforeEach
    void init() {
        client = new Client("jiri", "sob", "asd@gmail.com");
        completeOrderRequest = new CompleteOrderRequest();
        completeOrderRequest.setClient(client);
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("Harry Potrat",
                "Roulingova",
                1500,
                BookCarrierType.ELECTRONIC_TEXT,
                "Luxor",
                "fantasy"));

        completeOrderRequest.setBooks(bookList);
    }


    @Test
    @DisplayName("test listen function when clientRepository return client")
    void listenFunctionReturnClient() {
        when(clientRepository.findByEmail(anyString())).thenReturn(client);
        orderService.listen(completeOrderRequest);
        verify(clientRepository, atLeast(0)).save(any());
    }

    @Test
    @DisplayName("test listen function when clientRepository do not return client")
    void listenFunctionNotClient() {
        when(clientRepository.findByEmail(anyString())).thenReturn(null);
        orderService.listen(completeOrderRequest);
        verify(clientRepository, atLeast(1)).save(any());
    }
}

