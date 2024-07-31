package com.ashu.jwtsecurity.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ashu.jwtsecurity.dto.AuthenticationRequest;
import com.ashu.jwtsecurity.dto.AuthenticationResponse;
import com.ashu.jwtsecurity.dto.RegisterRequest;
import com.ashu.jwtsecurity.model.Role;
import com.ashu.jwtsecurity.model.User;
import com.ashu.jwtsecurity.repo.UserRepository;
import com.ashu.jwtsecurity.utils.JwtUtils;

@Service
public record AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
		AuthenticationManager authenticationManager) {

	public AuthenticationResponse register(RegisterRequest request) {
		final var user = new User(null, request.firstname(), request.lastname(), request.email(),
				passwordEncoder.encode(request.password()), Role.USER);
		userRepository.save(user);

		final var token = JwtUtils.generateToken(user);
		return new AuthenticationResponse(token);

	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
		final var user = userRepository.findByEmails(request.email()).orElseThrow();
		final var token = JwtUtils.generateToken(user);
		return new AuthenticationResponse(token);

	}
}
