package com.salman.payflow.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.salman.payflow.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	
	public JwtAuthenticationFilter(JwtService jwtService){
		this.jwtService=jwtService;
	}
	
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response , FilterChain filterChain) 
		throws ServletException, IOException{
		String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader!=null && authHeader.startsWith("Bearer ")) {
			String token= authHeader.substring(7);
			if (jwtService.isTokenValid(token)) {
				String username = jwtService.extractUsername(token);
				
				UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(username,null,AuthorityUtils.NO_AUTHORITIES);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}

}
