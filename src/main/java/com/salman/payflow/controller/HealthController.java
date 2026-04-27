package com.salman.payflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	
	@GetMapping("/health")
	public String health() {
		return ("payflow API is running ");
		
	}	
	

}
