package com.salman.payflow.exception;

public class SameWalletException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	 public SameWalletException() {
	        super("Sender and receiver cannot be the same wallet");
	    }
	
}
