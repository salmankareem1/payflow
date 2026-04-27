package com.salman.payflow;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config= new CorsConfiguration();
		
		 // which frontend origins are allowed to call this API
		config.setAllowedOrigins(List.of("http://localhost:3000"));
		
		// which HTTP methods are allowed
		config.setAllowedHeaders(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		
		// which headers the client is allowed to send
		config.setAllowedHeaders(List.of("*"));
		
		//  // allow the Authorization header to be sent — needed for JWT
		config.setAllowCredentials(true);
		
		// apply this config to every endpoint in the application
		UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return source;
		
	}
	
}
