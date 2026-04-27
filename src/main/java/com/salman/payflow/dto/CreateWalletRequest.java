package com.salman.payflow.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateWalletRequest {

	@NotBlank(message="User ID cannot be blank")
	private String userId;
	
	@NotBlank(message="Currency cannot be blank")
	private String currency;
	
	public String getUserId()
	{
		return userId;
	}
	
	public String setUserId(String userId) {
		return this.userId=userId;
	}
	
	public String getCurrency()
	{
		return currency;
	}
	
	public String setCurrency(String currency) {
		return this.currency=currency;
	}
}
