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

package com.pras.account.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pras.account.user.jwt.JwtFilter;

/**
 * Customized security configurations to protect end points.
 * 
 * @author Prasanta Paul
 */
@Configuration
@EnableWebSecurity // Enable Web Security Support (via HttpSecurity)
public class SecurityConfiguration {
	
	JwtFilter jwtFilter;
	LoginUtilityService loginUtilityService;
	
	public SecurityConfiguration(JwtFilter jwtFilter, LoginUtilityService loginUtilityService) {
		// Auto inject dependent beans
		this.jwtFilter = jwtFilter;
		this.loginUtilityService = loginUtilityService;
	}

	// Security Filter Chain for API
	@Bean
	@Order(1)
	public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity http) throws Exception {
		// JWT based Token Authentication is enabled
		// To enable Basic Authentication, comment addFilterBefore(jwtFilter,...) and uncomment httpBasic
		return http
				.securityMatcher("/api/**")
				.authorizeHttpRequests(request -> {
					request.requestMatchers("/api/open/**").permitAll();
					request.anyRequest().authenticated();
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				//.httpBasic(Customizer.withDefaults()) // Basic Authentication (Disable)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Filter to process JWT
				.csrf(csrf -> csrf.disable()) // Disable CSRF to allow modifying requests (POST) at open endpoints
				.build();
	}
	
	// Security Filter Chain for Web Pages
//	@Bean
//	@Order(2)
//	public SecurityFilterChain formSecurityFilterChain(HttpSecurity http) throws Exception {
//		// Form Login for Web Pages
//		return http.authorizeHttpRequests(request -> {
//			request.requestMatchers("/").permitAll();
//			request.requestMatchers("/error").permitAll();
//			request.requestMatchers("/open").permitAll();
//			request.anyRequest().authenticated();
//		})
//		.formLogin((formLoginConfig) -> formLoginConfig.defaultSuccessUrl("/protected", true))
//		.logout(logoutConfig -> logoutConfig.logoutSuccessUrl("/"))
//		.build();
//	}
	
	// Ignore selected URIs from security checks
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
		// Ignore static directories from Security Filter Chain
        return web -> web.ignoring().requestMatchers("/images/**", "/js/**");
    }
	
	// Custom User Details Service to manage login
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetailsService userDetailsService = (userName) -> {
			return loginUtilityService.findMatch(userName);
		};
		return userDetailsService;
	}
	
	// Password encoder
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
	
	// TODO: Enable CORS for JWT 
}
