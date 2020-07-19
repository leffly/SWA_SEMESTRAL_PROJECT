package cz.cvut.fel.swa.store.service;

import cz.cvut.fel.swa.store.config.KafkaTopicConfig;
import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class CompleteOrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    public CompleteOrderService(KafkaTemplate<String, CompleteOrderRequest> kafkaTemplate, KafkaTopicConfig kafkaTopicConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    public void sendCompleteOrderMessage(CompleteOrderRequest completeOrderRequest) {
        ListenableFuture<SendResult<String, CompleteOrderRequest>> future = kafkaTemplate.send(kafkaTopicConfig.complementOrder().name(), completeOrderRequest);

        logger.info("Kafka message prepared - {}", completeOrderRequest);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, CompleteOrderRequest> result) {
                logger.info("[COMPLEMENT_ORDER] Sent message=[{}] with offset=[{}]", completeOrderRequest, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("[COMPLEMENT_ORDER] Unable to send message=[{}] due to exception", completeOrderRequest, ex);
            }
        });
    }

}
