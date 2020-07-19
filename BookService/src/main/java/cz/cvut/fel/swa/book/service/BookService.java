package cz.cvut.fel.swa.book.service;

import cz.cvut.fel.swa.book.config.KafkaTopicConfig;
import cz.cvut.fel.swa.book.dto.BookOfferDTO;
import cz.cvut.fel.swa.book.model.Book;
import cz.cvut.fel.swa.book.repository.BookRepository;
import cz.cvut.fel.swa.book.request.CompleteOrderRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BookService {

    private final Logger LOGGER = Logger.getLogger(BookService.class.getName());
    private final BookRepository bookRepository;
    private final KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    public BookService(BookRepository bookRepository, KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate, KafkaTopicConfig kafkaTopicConfig) {
        this.bookRepository = bookRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public void save(Book book){
        bookRepository.save(book);
    }

    public void save(List<Book> books){
        bookRepository.saveAll(books);
    }

    public BookOfferDTO findBookOfferByGenre(String genre){
        return new BookOfferDTO(bookRepository.findAllByGenre(genre));
    }

    public void sendCalculateOrderMessage(CompleteOrderRequest completeOrderRequest) {
        ListenableFuture<SendResult<String, CompleteOrderRequest>> future =
                kafkaTemplate.send(kafkaTopicConfig.calculateOrder().name(), completeOrderRequest);

        LOGGER.info("Kafka message prepared - " + completeOrderRequest.toString());

        future.addCallback(new ListenableFutureCallback<SendResult<String, CompleteOrderRequest>>() {
            @Override
            public void onSuccess(SendResult<String, CompleteOrderRequest> result) {
                LOGGER.log(Level.INFO,"[CALCULATE_ORDER] Sent message=[" + completeOrderRequest.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.log(Level.SEVERE,"[CALCULATE_ORDER] Unable to send message=["
                        + completeOrderRequest.toString() + "] due to : " + ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "${complementOrderTopic}",
            groupId = "${bookGroupId}",
            containerFactory = "completeOrderRequestContainerFactory")
    public void listen(CompleteOrderRequest completeOrderRequest) {
        LOGGER.log(Level.INFO,"[COMPLEMENT_ORDER] Received Kafka Messasge: " + completeOrderRequest.toString());

        completeOrderRequest.getBooks().forEach(book -> {
            LOGGER.log(Level.INFO,"[COMPLEMENT_ORDER] Searching for: " + book.toString());
            List<Book> foundList = bookRepository.findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(
                    book.getAuthor(), book.getName(), book.getCarrierType());
            LOGGER.log(Level.INFO,"[COMPLEMENT_ORDER] Search ended up with " +
                    ((foundList == null) ? 0 : foundList.size()) + " results.");
            if(foundList != null && foundList.size() > 0){
                book.setGenre(foundList.get(0).getGenre());
                book.setId(foundList.get(0).getId());
                book.setPrice(foundList.get(0).getPrice());
                book.setSupplier(foundList.get(0).getSupplier());
                LOGGER.log(Level.INFO,"[COMPLEMENT_ORDER] Books harvested metadata: " + book.toString());
            }
        });

        this.sendCalculateOrderMessage(completeOrderRequest);
    }

}
