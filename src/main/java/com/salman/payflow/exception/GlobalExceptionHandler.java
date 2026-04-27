package com.salman.payflow.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	//404- resource not found 
	@ExceptionHandler(WalletNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String,String> handleWalletNotFoundException(WalletNotFoundException e){
		log.warn("Wallet not found : {}",e.getMessage());
		
		return Map.of("message",e.getMessage());
		
	}
	
	//422 - valid request but business rule failed
	@ExceptionHandler(InsufficientFundsException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public Map<String,String> handleInsufficientBalanceException(InsufficientFundsException e){
		log.warn("Insufficient funds:{}",e.getMessage());
		return Map.of("message",e.getMessage());
			
	}
	
	//409-conflict, Duplicate transaction
	@ExceptionHandler(DuplicateTransferException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String,String> handleDuplicatetransferException(DuplicateTransferException e){
		log.warn("Duplicate transaction:{}",e.getMessage());
		return Map.of("message",e.getMessage());
	}
	
	//400- bad request, currency mismatch
	@ExceptionHandler(CurrencyMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,String> handleCurrencyMismatchException(CurrencyMismatchException e){
		log.warn("Currency Mismatch:{}",e.getMessage());
		return Map.of("message",e.getMessage());
	}
	
	//400 - bad request, same wallet
	@ExceptionHandler(SameWalletException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,String> handleSameWalletException(SameWalletException e){
		log.warn("Same Wallet transfer Attempt: {}",e.getMessage());
		return Map.of("message",e.getMessage());
	}
	
	
	//400 — @Valid constraint violations
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)

	public Map<String,String> handleValidationException(MethodArgumentNotValidException e){
		
		
		Map<String,String> errors= new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach(error-> errors.put(error.getField(),error.getDefaultMessage()));
		
		return errors ;
	}
	
	// 400 — any other runtime exceptions
@ExceptionHandler(RuntimeException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)

public Map<String, String> handleRuntimeException(RuntimeException e){
	log.warn("Bad request: {}", e.getMessage()); 
	Map <String,String> errors = new HashMap<>();
	
	errors.put("message",e.getMessage());
	
	return errors ;
}



// 500 — unexpected server errors, bugs, DB failures
@ExceptionHandler(Exception.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public Map<String, String> handleUnexpectedException(Exception e) {
	log.error("Unexpected server error", e); 
	Map<String, String> errors = new HashMap<>();
	errors.put("message", "An unexpected error occurred. Please try again later.");
	return errors;
}

}
