package com.salman.payflow.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventConsumer {

private static final Logger log = LoggerFactory.getLogger(TransactionEventConsumer.class);

@KafkaListener(
		topics = "payflow.transaction.events",
		groupId="payflow-group"
		)
public void consume(TransactionEvent event) {
	log.info("Transaction event received:{}",event);
	log.info("Transfer of {} from wallet {} to wallet {} completed with status {}", event.getAmount(),event.getFromWalletId(),event.getToWalletId(),event.getStatus() );
}
	
}
