package com.salman.payflow.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionEvent {
	private Long fromWalletId;
	private Long toWalletId;
	private BigDecimal amount;
	private String referenceId;
	private String status;
	private LocalDateTime timestamp;
	
   public TransactionEvent() {}
   public TransactionEvent(Long fromWalletId,
	 Long toWalletId,
	 BigDecimal amount,
	 String referenceId, String status) {
	   this.fromWalletId=fromWalletId;
	   this.toWalletId=toWalletId;
	   this.amount=amount;
	   this.referenceId=referenceId;
	   this.status=status;
	   this.timestamp=LocalDateTime.now();
   }
   public Long getFromWalletId() {
	   return fromWalletId;
   }
   
   public void setFromWalletId(Long fromWalletId) {
	   this.fromWalletId=fromWalletId;
   }
   public Long getToWalletId() {
	   return toWalletId;
   }
   
   public void setToWalletId(Long toWalletId) {
	   this.toWalletId=toWalletId;
   }
   
   public BigDecimal getAmount() {
	   return amount;
   }
   
   public void setAmount(BigDecimal amount) {
	   this.amount=amount;
   }
   public String getReferenceId() {
	   return referenceId;
   }
   
   public void setReferenceId(String referenceId) {
	   this.referenceId=referenceId;
   }
   public String getStatus() {
	   return status;
   }
   
   public void setStatus(String status) {
	   this.status=status;
   }
   public LocalDateTime getTimestamp() {
	   return timestamp;
   }
   
   public void setTimestamp(LocalDateTime timestamp) {
	   this.timestamp=timestamp;
   }
   
   @Override
   public String toString() {
       return "TransactionEvent{" +
               "fromWalletId=" + fromWalletId +
               ", toWalletId=" + toWalletId +
               ", amount=" + amount +
               ", status='" + status + '\'' +
               ", referenceId='" + referenceId + '\'' +
               ", timestamp=" + timestamp +
               '}';
   }
}
