package com.salman.payflow.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "From wallet id is required")
	private Long fromWalletId;
	@NotNull(message = "To wallet id is required")
	private Long toWalletId;
	private BigDecimal amount;
	
	private String status;
	private String referenceId;
	
	public Long getId() {
		return id;
	}
	
	public Long setId() {
		return id;
	}
	
	public void setFromWalletId(Long fromWalletId) {
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
	public void setStatus(String status) {
		this.status=status;
	}
	
	public String getStatus() {
		return status;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId=referenceId;
	}
	
	public String getReferenceId() {
		return referenceId;
	}
	
}
