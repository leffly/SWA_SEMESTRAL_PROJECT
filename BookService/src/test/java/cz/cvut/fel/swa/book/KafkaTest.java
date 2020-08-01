package cz.cvut.fel.swa.book;


import cz.cvut.fel.swa.book.enums.BookCarrierType;
import cz.cvut.fel.swa.book.model.Book;
import cz.cvut.fel.swa.book.model.Client;
import cz.cvut.fel.swa.book.repository.BookRepository;
import cz.cvut.fel.swa.book.request.CompleteOrderRequest;
import cz.cvut.fel.swa.book.service.BookService;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@EmbeddedKafka(partitions = 1)
@SpringBootTest
public class KafkaTest {

    @Value(value = "${calculateOrder}")
    private String calculateOrderTopic;

    @Mock
    BookRepository bookRepository;

    @Mock
    SendResult<String, CompleteOrderRequest> sendResult;


    @Mock
    ListenableFuture<SendResult<String, CompleteOrderRequest>> responseFuture;


    @Mock
    private KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;


    @InjectMocks
    @Resource
    BookService bookService;

    List<Book> mockBookList = new ArrayList<>();
    CompleteOrderRequest completeOrderRequest = createMockCompleteOrderRequest();
    RecordMetadata recordMetadata;

    @BeforeEach
    void init() {
        recordMetadata = new RecordMetadata(new TopicPartition(calculateOrderTopic, 1), 1, 0L, 0L, 0L, 0, 0);
        Book book = new Book("name", "autor", 100, BookCarrierType.ELECTRONIC_TEXT, "alza", "scifi");
        mockBookList.add(book);
    }

    @Test
    void kafkaLogicTest() {

        when(bookRepository.findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(anyString(), anyString(), any()))
                .thenReturn(mockBookList);
        given(sendResult.getRecordMetadata()).willReturn(recordMetadata);
        when(kafkaTemplate.send(calculateOrderTopic, completeOrderRequest))
                .thenReturn(responseFuture);
        doAnswer(
                invocationOnMock -> {
                    ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
                    listenableFutureCallback.onSuccess(sendResult);
                    assertThat(sendResult.getRecordMetadata().offset()).isEqualTo(1);
                    assertThat(sendResult.getRecordMetadata().partition()).isEqualTo(1);
                    return null;
                }).when(responseFuture).addCallback(any(ListenableFutureCallback.class));

        bookService.listen(createMockCompleteOrderRequest());
        verify(bookRepository, atMost(mockBookList.size()))
                .findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(anyString(), anyString(), any());
    }

    public static CompleteOrderRequest createMockCompleteOrderRequest() {
        CompleteOrderRequest completeOrderRequest = new CompleteOrderRequest();
        Client client = new Client("geore", "pocural", "geo@gmail.com");
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("name", "autor", 100, BookCarrierType.ELECTRONIC_TEXT, "alza", "scifi"));
        completeOrderRequest.setBooks(bookList);
        completeOrderRequest.setClient(client);
        return completeOrderRequest;
    }
}
