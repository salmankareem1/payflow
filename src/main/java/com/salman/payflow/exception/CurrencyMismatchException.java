package com.salman.payflow.exception;

public class CurrencyMismatchException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	 public CurrencyMismatchException(String from, String to) {
	        super("Currency mismatch: cannot transfer from " + from + " to " + to);
	    }

}
