package com.salman.payflow.dto;

import java.math.BigDecimal;

public class TransactionResponse {

	
	private Long id;
	
	private Long fromWalletId;
	
	private Long toWalletId;
	private BigDecimal amount;
	
	private String status;
	
	private String referenceId;
	
	
	public TransactionResponse() {
		
	}
    public TransactionResponse(Long id, Long fromWalletId, Long toWalletId, BigDecimal amount, String status, String referenceId) {
	        this.id = id;
	        this.fromWalletId = fromWalletId;
	        this.toWalletId = toWalletId;
	        this.amount = amount;
	        this.status = status;
	        this.referenceId=referenceId;
	    }
	public Long getId() {
		return id;
	}
	

	
	public Long getFromWalletId() {
		return fromWalletId;
	}
	

	public Long getToWalletId() {
		return toWalletId;
	}

	
	public BigDecimal getAmount() {
		return amount;
	}

	
	public String getStatus() {
		return status;
	}
	
	public String getReferenceId() {
		return referenceId;
	}
}
