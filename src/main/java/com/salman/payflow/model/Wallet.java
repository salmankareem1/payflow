package com.salman.payflow.model;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="wallet")
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NotBlank(message="User Id cannot be blank")
	@Column(name="user_Id",nullable=false)
	private String userId;
	
	@Column(precision=16,scale=4,nullable=false)
	private BigDecimal balance;
	
	@NotBlank(message="Currency cannot be blank")
	@Column(nullable=false,length=10)
	private String currency;
	@Version
	private Long version;
	
	public Wallet() {
	}
	
	public Wallet(Long id, String userId, BigDecimal balance, String currency,Long version) {
		this.id=id;
		this.userId=userId;
		this.balance=balance;
		this.currency=currency;
        this.version=version;
		
	}
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
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