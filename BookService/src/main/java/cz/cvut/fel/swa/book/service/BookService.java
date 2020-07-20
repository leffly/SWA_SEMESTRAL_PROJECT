package cz.cvut.fel.swa.book.service;

import cz.cvut.fel.swa.book.config.KafkaTopicConfig;
import cz.cvut.fel.swa.book.dto.BookOfferDTO;
import cz.cvut.fel.swa.book.model.Book;
import cz.cvut.fel.swa.book.repository.BookRepository;
import cz.cvut.fel.swa.book.request.CompleteOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

public class BookService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate, KafkaTopicConfig kafkaTopicConfig) {
        this.bookRepository = bookRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void save(List<Book> books) {
        bookRepository.saveAll(books);
    }

    public BookOfferDTO findBookOfferByGenre(String genre) {
        return new BookOfferDTO(bookRepository.findAllByGenre(genre));
    }

    private void sendCalculateOrderMessage(CompleteOrderRequest completeOrderRequest) {
        ListenableFuture<SendResult<String, CompleteOrderRequest>> future =
                kafkaTemplate.send(kafkaTopicConfig.calculateOrder().name(), completeOrderRequest);

        log.info("Kafka message prepared - " + completeOrderRequest.toString());

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, CompleteOrderRequest> result) {
                log.info("[CALCULATE_ORDER] Sent message=[{}] with offset=[{}]", completeOrderRequest, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("[CALCULATE_ORDER] Unable to send message=[{}] due to exception", completeOrderRequest, ex);
            }
        });
    }

    @KafkaListener(topics = "${complementOrderTopic}",
            groupId = "${bookGroupId}",
            containerFactory = "completeOrderRequestContainerFactory")
    public void listen(CompleteOrderRequest completeOrderRequest) {
        log.info("[COMPLEMENT_ORDER] Received Kafka Message: {}", completeOrderRequest);

        completeOrderRequest.getBooks().forEach(book -> {
            log.info("[COMPLEMENT_ORDER] Searching for: {}", book);
            List<Book> foundList = bookRepository.findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(
                    book.getAuthor(), book.getName(), book.getCarrierType());
            log.info("[COMPLEMENT_ORDER] Search ended up with {} results", ((foundList == null) ? 0 : foundList.size()));
            if (foundList != null && foundList.size() > 0) {
                book.setGenre(foundList.get(0).getGenre());
                book.setId(foundList.get(0).getId());
                book.setPrice(foundList.get(0).getPrice());
                book.setSupplier(foundList.get(0).getSupplier());
                log.info("[COMPLEMENT_ORDER] Books harvested metadata: {}", book);
            }
        });

        this.sendCalculateOrderMessage(completeOrderRequest);
    }

}
