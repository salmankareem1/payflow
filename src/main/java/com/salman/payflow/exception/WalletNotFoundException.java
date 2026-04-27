package com.salman.payflow.exception;

public class WalletNotFoundException extends RuntimeException{
	 private static final long serialVersionUID = 1L;
 public WalletNotFoundException(Long id) {
	 super("wallet not found with id:"+ id);
 }
	
	
}
