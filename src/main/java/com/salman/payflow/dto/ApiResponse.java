package com.salman.payflow.dto;

public class ApiResponse<T> {

	
	private String message;
	private T data;
	
	public ApiResponse()
	{}	
	public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
	public void setMessage(String message) {
		this.message=message;	}
	
	
	public String getMessage( ) {
		return message;
	}
	
	public void setData(T data) {
		this.data=data;
	}
	
	public T getdata() {
		return data;
	}
}
