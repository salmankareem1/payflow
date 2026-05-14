package com.salman.payflow;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI payflowOpenApi() {
		return new OpenAPI()
				
				.servers(List.of(
                        new Server().url("https://payflow-production-0543.up.railway.app")
                ))
				
				.info(new Info().title("Payflow API")
			    .version("v1")
			    .description("Production-grade payments API — wallet management,"
			    		+ "fund transfers with optimistic locking, and JWT authentication.")
			    .contact(new Contact().name("Salman Abdul Kareem").email("salmankareem9@gmail.com")))
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
						.name("bearerAuth")
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.description("Enter your JWT token from POST /auth/login")
						));
						
	}
	
	
}
