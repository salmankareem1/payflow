package com.salman.payflow.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
@ExceptionHandler(RuntimeException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)

public Map<String, String> handleRuntimeException(RuntimeException e){
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

}
