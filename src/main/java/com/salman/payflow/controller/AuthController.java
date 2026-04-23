package com.salman.payflow.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salman.payflow.dto.LoginRequest;
import com.salman.payflow.service.JwtService;


@RestController
@CrossOrigin(origins= "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

	private final  JwtService jwtService;
	
	public AuthController(JwtService jwtService) {
		this.jwtService=jwtService;
	}
	@PostMapping("/login")
	public Map<String,String> login (@RequestBody LoginRequest request){
		if (!"admin".equals(request.getUsername())||!"admin123".equals(request.getPassword())) {
			throw new RuntimeException("Invalid Username or Passwrod");
		}
		
	 String token= jwtService.generateToken(request.getUsername());
	 
	 Map<String, String> response= new HashMap<>(); 			
	 response.put("token",token);			
	 return response;			
	}
	
}

