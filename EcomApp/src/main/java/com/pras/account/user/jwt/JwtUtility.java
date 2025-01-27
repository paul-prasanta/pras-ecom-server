/*
 * Copyright (C) 2025- Prasanta Paul, https://github.com/paul-prasanta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pras.account.user.jwt;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * JWT (JSON Web Token) Utility class to manage token generation, verification and metadata extraction.
 * 
 * JWT consists of 3 parts = [header].[payload].[signature]. Payload contains Claims which 
 * includes subject (username or ID), expiry time, additional business specific meta data etc.
 * 
 * Secrete Key (SHA-256 Hash) is used to verify tokens against content tempering.
 * 
 * @see JWT Specification: https://jwt.io/
 * @see JWT Library: https://github.com/jwtk/jjwt
 * 
 * @author Prasanta Paul
 */
@Service
public class JwtUtility {

	/*
	 * Generator App: https://www.akto.io/tools/hmac-sha-256-hash-generator
	 * 
	 * Text: "Happy World 2024"
	 * Key: "Key2024"
	 * Algorithm: SHA-256 Hash (in Hex)
	 */
	@Value("${com.secapp.jwt.secret}")
	private String SECRET_KEY;
	
	public String generateToken(Map<String, String> extraClaims, String userName, long expireInterval) {
		return Jwts
                .builder()
                .claims().add(extraClaims)
                .and()
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireInterval))
                .signWith(getSignInKey())
                .compact();
	}
	
	public String getUserName(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}
	
	public boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getExpiration().before(new Date());
	}
	
	private Claims extractAllClaims(String token) {
		// Extract claims after signature verification 
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
	
	private SecretKey getSignInKey() {
		System.out.println("secrete: "+ SECRET_KEY);
		byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	// Test Tokens
//	public static void main(String[] args) {
//		Map<String, String> metadata = new HashMap<String, String>();
//		metadata.put("id", "1xdf100");
//		metadata.put("role", "ROLE_ADMIN");
//		int validityInterval = 24 * 60 * 60 * 1000; // 1 day validity
//		
//		JwtUtility jwtUtility = new JwtUtility();
//		String token = jwtUtility.generateToken(metadata, "adminUser", validityInterval);
//		System.out.println("JWT Token: "+ token);
//		
//		Claims claims = jwtUtility.extractAllClaims(token);
//		
//		System.out.println("Subject: "+ claims.getSubject());
//		System.out.println("Extras: "+ claims.keySet());
//	}
}
