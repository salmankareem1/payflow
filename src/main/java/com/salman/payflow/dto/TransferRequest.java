package com.salman.payflow.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferRequest {

	
	@NotNull(message="From wallet Id shouldn't be null")
	private Long fromWalletId;
	
	@NotNull(message="To wallet Id shouldn't be null")
	private Long toWalletId;
	
	@NotNull(message="Amount shouldn't be null")
	@Positive(message="The Amount should be greater than zero")
	private BigDecimal amount;
	
	
	public void setfromWalletId(Long fromWalletId) {
		this.fromWalletId=fromWalletId;
	}
	
	public Long getFromWalletId() {
		return fromWalletId;
	}
	
	public void setToWalletId(Long toWalletId) {
		this.toWalletId=toWalletId;
	}
	
	public Long getToWalletId() {
		return toWalletId;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount=amount;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	
}
