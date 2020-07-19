package cz.cvut.fel.swa.store.service;

import cz.cvut.fel.swa.store.config.KafkaTopicConfig;
import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CompleteOrderService {

    private final KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    private final Logger logger = Logger.getLogger(CompleteOrderService.class.getName());

    public CompleteOrderService(KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate, KafkaTopicConfig kafkaTopicConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    public void sendCompleteOrderMessage(CompleteOrderRequest completeOrderRequest) {
        ListenableFuture<SendResult<String, CompleteOrderRequest>> future =
                kafkaTemplate.send(kafkaTopicConfig.complementOrder().name(), completeOrderRequest);

        logger.info("Kafka message prepared - " + completeOrderRequest.toString());

        future.addCallback(new ListenableFutureCallback<SendResult<String, CompleteOrderRequest>>() {
            @Override
            public void onSuccess(SendResult<String, CompleteOrderRequest> result) {
                logger.log(Level.INFO,"[COMPLEMENT_ORDER] Sent message=[" + completeOrderRequest.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                logger.log(Level.SEVERE,"[COMPLEMENT_ORDER] Unable to send message=["
                        + completeOrderRequest.toString() + "] due to : " + ex.getMessage());
            }
        });
    }

}
