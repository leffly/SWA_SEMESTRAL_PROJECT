package cz.cvut.fel.swa.book.service;

import cz.cvut.fel.swa.book.KafkaTest;
import cz.cvut.fel.swa.book.enums.BookCarrierType;
import cz.cvut.fel.swa.book.model.Book;
import cz.cvut.fel.swa.book.repository.BookRepository;
import cz.cvut.fel.swa.book.request.CompleteOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@EmbeddedKafka(partitions = 1, topics = {"testTopic"})
@SpringBootTest
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Autowired
    KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;

    @InjectMocks
    BookService bookService;

    List<Book> mockBookList = new ArrayList<>();
    ;

    @BeforeEach
    void init() {
        Book book = new Book("name", "autor", 150, BookCarrierType.ELECTRONIC_TEXT, "CZC", "Horor");
        mockBookList.add(book);
    }

    @Test
    void basicFindAllBooks() {
        List<Book> bookList = new ArrayList<>();
        Book book = new Book("name", "autor", 100, BookCarrierType.ELECTRONIC_TEXT, "alza", "scifi");
        bookList.add(book);
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> exepted = bookService.findAll();
        verify(bookRepository, atLeastOnce()).findAll();
        assertEquals(exepted.get(0).getAuthor(), "autor");
    }

    @Test
    void kafkaLogicTest() {
        CompleteOrderRequest request = KafkaTest.createMockCompleteOrderRequest();
        when(bookRepository.findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(anyString(), anyString(), any()))
                .thenReturn(mockBookList);
        try {
            bookService.listen(request);
        } catch (NullPointerException e) {
        }
        assertEquals(150, request.getBooks().get(0).getPrice());
        assertEquals("CZC", request.getBooks().get(0).getSupplier());
        assertEquals("Horor", request.getBooks().get(0).getGenre());
        verify(bookRepository, atLeastOnce())
                .findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(anyString(), anyString(), any());
    }
}
