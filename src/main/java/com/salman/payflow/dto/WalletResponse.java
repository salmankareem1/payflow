package com.salman.payflow.dto;

import java.math.BigDecimal;

public class WalletResponse {

	private Long id;
	private String userId;
	private String currency;
	private BigDecimal balance;
	private Long version;
	
	
	public WalletResponse() {
		
	}
	

	    public WalletResponse(Long id, String userId, BigDecimal balance, String currency,Long version) {
	        this.id = id;
	        this.userId = userId;
	        this.balance = balance;
	        this.currency = currency;
	        this.version=version;
	    }

	    public Long getId() {
	        return id;
	    }
	    
	    public Long getversion() {
	    	return version;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public BigDecimal getBalance() {
	        return balance;
	    }

	    public String getCurrency() {
	        return currency;
	    }
	
}
