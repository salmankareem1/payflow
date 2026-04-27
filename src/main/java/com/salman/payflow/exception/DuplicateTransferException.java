package com.salman.payflow.exception;

public class DuplicateTransferException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DuplicateTransferException(String referenceId) {
		super("The transaction already exists with refernce Id:"+ referenceId);
	}
	
}
