package cz.cvut.fel.swa.store;


import cz.cvut.fel.swa.store.enums.BookCarrierType;
import cz.cvut.fel.swa.store.model.Book;
import cz.cvut.fel.swa.store.model.Client;
import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import cz.cvut.fel.swa.store.service.CompleteOrderService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@EmbeddedKafka(partitions = 1)
@SpringBootTest
public class KafkaTest {

    @Value(value = "${complementOrderTopic}")
    private String complementOrderTopic;

    @Mock
    SendResult<String, CompleteOrderRequest> sendResult;

    @Mock
    ListenableFuture<SendResult<String, CompleteOrderRequest>> responseFuture;

    @InjectMocks
    @Resource
    CompleteOrderService completeOrderService;

    @Mock
    private KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;


    RecordMetadata recordMetadata;
    List<Book> mockBookList = new ArrayList<>();

    @BeforeEach
    void init() {
        recordMetadata = new RecordMetadata(new TopicPartition(complementOrderTopic, 1), 1, 0L, 0L, 0L, 0, 0);
        Book book = new Book("name", "autor", BookCarrierType.ELECTRONIC_TEXT);
        mockBookList.add(book);
    }

    @Test
    void kafkaLogicTest() {
        CompleteOrderRequest completeOrderRequest = createMock();

        given(sendResult.getRecordMetadata()).willReturn(recordMetadata);
        when(kafkaTemplate.send(complementOrderTopic, completeOrderRequest))
                .thenReturn(responseFuture);
        doAnswer(
                invocationOnMock -> {
                    ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
                    listenableFutureCallback.onSuccess(sendResult);
                    assertThat(sendResult.getRecordMetadata().offset()).isEqualTo(1);
                    assertThat(sendResult.getRecordMetadata().partition()).isEqualTo(1);
                    return null;
                }).when(responseFuture).addCallback(any(ListenableFutureCallback.class));

        completeOrderService.sendCompleteOrderMessage(createMock());
    }


    private CompleteOrderRequest createMock() {
        CompleteOrderRequest completeOrderRequest = new CompleteOrderRequest();
        Client client = new Client("geore", "pocural", "geo@gmail.com");
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("name", "autor", BookCarrierType.ELECTRONIC_TEXT));
        completeOrderRequest.setBooks(bookList);
        completeOrderRequest.setClient(client);
        return completeOrderRequest;
    }

}
