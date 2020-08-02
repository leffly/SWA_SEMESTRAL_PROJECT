package cz.cvut.fel.swa.order;

import cz.cvut.fel.swa.order.enums.BookCarrierType;
import cz.cvut.fel.swa.order.model.Book;
import cz.cvut.fel.swa.order.model.Client;
import cz.cvut.fel.swa.order.model.Order;
import cz.cvut.fel.swa.order.model.OrderedBooks;
import cz.cvut.fel.swa.order.repository.BookRepository;
import cz.cvut.fel.swa.order.repository.ClientRepository;
import cz.cvut.fel.swa.order.repository.OrderRepository;
import cz.cvut.fel.swa.order.repository.OrderedBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class JPATests {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    OrderedBooksRepository orderedBooksRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookRepository bookRepository;

    Order mockOrder;
    Client client;
    Book mockBook;
    List<Book> mockListBook;

    @BeforeEach
    void init() {
        mockListBook = createMockListBook();
        client = new Client("jirka", "Past", "asd@gmail.com");
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(bookRepository).isNotNull();
        assertThat(orderRepository).isNotNull();
        assertThat(clientRepository).isNotNull();
        assertThat(orderedBooksRepository).isNotNull();
    }

    @Test
    void clientFind() {
        clientRepository.save(client);
        assertThat(clientRepository.findByEmail("asd@gmail.com")).isNotNull();
    }

    @Test
    void findAllByClient_Email() {
        List<Book> bookList = createMockListBook();
        bookRepository.saveAll(bookList);
        clientRepository.save(client);
        assertThat(bookRepository.findAll().size()).isEqualTo(2);

        mockOrder = new Order();
        mockOrder.setId(1l);
        mockOrder.setClient(client);

        List<OrderedBooks> listOlBooks = mockListBook.stream().map(book -> {
            OrderedBooks orderedBooks = new OrderedBooks();
            orderedBooks.setBook(book);
            orderedBooks.setOrder(mockOrder);
            return orderedBooks;
        }).collect(Collectors.toList());

        mockOrder.setBooks(listOlBooks);

        orderRepository.save(mockOrder);
        assertThat(orderRepository.findAllByClient_Email("asd@gmail.com")).isNotEmpty();
    }


    private List<Book> createMockListBook() {
        List result = new ArrayList();
        mockBook = new Book("Harry Potrat",
                "Roulingova",
                1500,
                BookCarrierType.ELECTRONIC_TEXT,
                "Luxor",
                "fantasy");
        result.add(mockBook);
        result.add(new Book("Labirint",
                "Unknown",
                500,
                BookCarrierType.ELECTRONIC_TEXT,
                "Luxor",
                "fantasy"));
        return result;
    }
}
