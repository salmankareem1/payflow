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
	
@ExceptionHandler(RuntimeException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)

public Map<String, String> handleRuntimeException(RuntimeException e){
	log.warn("Bad request: {}", e.getMessage()); 
	Map <String,String> errors = new HashMap<>();
	
	errors.put("message",e.getMessage());
	
	return errors ;
}

@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)

public Map<String,String> handleValidationException(MethodArgumentNotValidException e){
	Map<String,String> errors= new HashMap<>();
	e.getBindingResult().getFieldErrors().forEach(error-> errors.put(error.getField(),error.getDefaultMessage()));
	
	return errors ;
}

@ExceptionHandler(Exception.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public Map<String, String> handleUnexpectedException(Exception e) {
	log.error("Unexpected error — this is a server bug, not a client error", e); 
	Map<String, String> errors = new HashMap<>();
	errors.put("message", "An unexpected error occurred. Please try again later.");
	return errors;
}

}
