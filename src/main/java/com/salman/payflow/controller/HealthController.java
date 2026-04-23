package com.salman.payflow.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HealthController {
	
	@GetMapping("/health")
	public String health() {
		return ("payflow API is running ");
		
	}	
	

}
