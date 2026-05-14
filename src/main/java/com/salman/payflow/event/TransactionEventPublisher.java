package com.salman.payflow.event;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(TransactionEventPublisher.class);
    private static final String TOPIC = "payflow.transaction.events";

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionEventPublisher(Optional<KafkaTemplate<String, TransactionEvent>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate.orElse(null);
    }

    public void publish(TransactionEvent event) {
        if (kafkaTemplate == null) {
            log.warn("Kafka is disabled, skipping event publish for referenceId={}", event.getReferenceId());
            return;
        }
        CompletableFuture<SendResult<String, TransactionEvent>> future = kafkaTemplate.send(TOPIC, event.getReferenceId(), event);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish TransactionEvent: referenceId={}, error={}", event.getReferenceId(), ex.getMessage());
            } else {
                log.info("TransactionEvent published successfully: referenceId={}, partition={}, offset={}",
                        event.getReferenceId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}