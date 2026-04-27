package com.salman.payflow.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salman.payflow.dto.ApiResponse;
import com.salman.payflow.dto.LoginRequest;
import com.salman.payflow.service.JwtService;


@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final  JwtService jwtService;
	
	public AuthController(JwtService jwtService) {
		this.jwtService=jwtService;
	}
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<String>> login (@RequestBody LoginRequest request){
		if (!"admin".equals(request.getUsername())||!"admin123".equals(request.getPassword())) {
			log.warn("Failed login attempt for username: {}", request.getUsername());
			
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Invalid username or password",null));
		}
		
		
	 String token= jwtService.generateToken(request.getUsername());
	 log.info("Successful login for username :{}",request.getUsername());
	 
	 return ResponseEntity.ok(new ApiResponse<>("Login successful",token));			
	}
	
}

