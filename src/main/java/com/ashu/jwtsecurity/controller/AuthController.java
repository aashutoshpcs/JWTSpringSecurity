package com.ashu.jwtsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ashu.jwtsecurity.dto.AuthenticationRequest;
import com.ashu.jwtsecurity.dto.AuthenticationResponse;
import com.ashu.jwtsecurity.dto.RegisterRequest;
import com.ashu.jwtsecurity.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(AuthenticationService authenticationService) {
	
	
	 @PostMapping("/register")
	    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
	        return ResponseEntity.ok(authenticationService.register(request));
	    }

	    @PostMapping("/authenticate")
	    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
	        return ResponseEntity.ok(authenticationService.authenticate(request));
	    }
	
}
