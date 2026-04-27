package com.salman.payflow.exception;

public class InsufficientFundsException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InsufficientFundsException() {
		super("Insufficient funds to complete the transfer");
	}
	
	
}
