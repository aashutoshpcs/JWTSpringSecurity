package com.ashu.jwtsecurity.utils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


@Component
public class JwtUtils {

//	@Value("jwt.secret")
//	private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static String secretKey = "mysecret98765432kjbhvhvlkjhgfdsxcfgvbnmnjbh";
	private static Key signingKey;
	
	private static long expirationTime = 1000 * 60 * 60;
	
	
	 @PostConstruct
	    public void init() {
	        // Ensure the key is at least 256 bits and Base64-encoded
	        byte[] keyBytes = secretKey.getBytes();
	        if (keyBytes.length < 32) {
	            throw new IllegalArgumentException("The secret key must be at least 32 bytes.");
	        }
	        secretKey = Base64.getEncoder().encodeToString(keyBytes);
	        signingKey = getSigningKey();
	    }

	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private static Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private static Key getSigningKey() {
		final var keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor((byte[]) keyBytes);
	}

	private static <C> C extractClaim(String token, Function<Claims, C> claimsResolver) {
		final var claims = extractClaims(token);
		return claimsResolver.apply(claims);
	}

	 public static String generateToken(UserDetails userDetails) {
		 Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, userDetails.getUsername());
	    }
	 
	 @SuppressWarnings("deprecation")
	private static String createToken(Map<String, Object> claims, String subject) {
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(subject)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//	                .signWith(SignatureAlgorithm.HS256, secretKey)
	                .signWith(signingKey)
	                .compact();
	    }

	public static boolean isTokenValid(String token, UserDetails userDetails) {
		final var username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private static boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
