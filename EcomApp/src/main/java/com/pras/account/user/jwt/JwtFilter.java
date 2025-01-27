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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.pras.account.user.config.LoginUtilityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT (JSON Web Token) Security Filter service. This is called as Before Filter
 * in Security Filter Chain.
 * 
 * Process flow: 
 * 1. Check request header for presence of Bearer Token 
 * 2. Read token, verify and extract claims (or metadata) 
 * 3. Check authentication state using "username" found in claims
 * 4. If not yet authenticated 
 * 4.1. Retrieve UserDetails instance from DB using "username" 
 * 4.2. Create Authentication instance using UsernamePasswordAuthenticationToken 
 * 4.3. Store Authentication Token in SecurityContext for Application to use
 * 
 * @author Prasanta Paul
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

	JwtUtility jwtUtility;
	LoginUtilityService loginUtilityService;
	HandlerExceptionResolver handlerExceptionResolver;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public JwtFilter(JwtUtility jwtUtility, LoginUtilityService loginUtilityService,
			HandlerExceptionResolver handlerExceptionResolver) {
		// Auto inject dependent beans
		this.jwtUtility = jwtUtility;
		this.loginUtilityService = loginUtilityService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			processToken(request);

		} catch (Exception e) {
			logger.error("Failed to process JWT Token: {}", e.getMessage());
			// Pass exceptions to response
			handlerExceptionResolver.resolveException(request, response, null, e);
		}

		logger.debug("Processing complete. Return back control to framework");

		// Pass the control back to framework
		filterChain.doFilter(request, response);
	}

	private void processToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		logger.info("Authorization Header: {}", authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.info("No Bearer Header, skip processing");
			return;
		}

		// Extract Bearer Token
		final String jwtToken = authHeader.substring(7);

		if (jwtUtility.isTokenExpired(jwtToken)) {
			logger.info("Token validity expired");
			return;
		}

		String userName = jwtUtility.getUserName(jwtToken);

		if (userName == null) {
			logger.info("No username found in JWT Token");
			return;
		}

		logger.info("Username found in JWT: " + userName);

		// Get existing authentication instance
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			logger.info("Already loggedin: " + userName);
			return;
		}

		// Authenticate and create authentication instance
		logger.info("Create authentication instance for {}", userName);
		UserDetails userDetails = loginUtilityService.findMatch(userName);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());

		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// Store authentication token for application to use
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}
