package com.salman.payflow.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "From wallet id is required")
	@Column(name="from_wallet_id",nullable=false)
	private Long fromWalletId;
	
	@NotNull(message = "To wallet id is required")
	@Column(name="to_wallet_id",nullable=false)
	private Long toWalletId;
	
	@Column(precision=16,scale=4,nullable=false)
	private BigDecimal amount;
	
	@Column(nullable =false)
	private String status;
	
	@Column(name="reference_id",unique=true)
	private String referenceId;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id= id;
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
