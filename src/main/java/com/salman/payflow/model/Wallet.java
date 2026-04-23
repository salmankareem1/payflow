package com.salman.payflow.model;


import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message="User Id cannot be blank")
	private String userId;
	private BigDecimal balance;
	@NotBlank(message="Currency cannot be blank")
	private String currency;
	@Version
	private Long version;
	
	public Wallet() {
	}
	
	public Wallet(long id, String userId, BigDecimal balance, String currency,Long version) {
		this.id=id;
		this.userId=userId;
		this.balance=balance;
		this.currency=currency;
        this.version=version;
		
	}
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id=id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId=userId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance=balance;
	}
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency=currency;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version=version;
	}
	
}