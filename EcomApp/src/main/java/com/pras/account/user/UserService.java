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

package com.pras.account.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pras.account.Customer;
import com.pras.account.CustomerRepository;
import com.pras.account.CustomerService;
import com.pras.account.user.Users.Role;
import com.pras.account.user.Users.Status;
import com.pras.account.user.jwt.JwtUtility;
import com.pras.utils.Exceptions.RegistrationException;

@Service
public class UserService {

	final int TOKEN_VALIDITY = 365 * 24 * 60 * 60 * 1000; // 1 year
	final String ADMIN_USERNAME = "admin";
	final String ADMIN_PASSWORD = "admin";
	
	@Autowired
	UsersRepository userRepository;
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	JwtUtility jwtUtility;
	@Autowired
	CustomerService customerService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Register Customer
	 * 
	 * @param user
	 * @return
	 */
	public Users register(Users user) {
		if(user.getPassword() == null || user.getPassword().length() < 4)
			throw new RegistrationException("Invalid Password, min 4 characters");
		
		// Create or Get existing Customer instance
		Customer customer = createOrGetCustomer(user.getCustomer());
		// Mobile Number becomes user name
		String userName = customer.getMobileNumber();
		
		List<Users> existing = userRepository.findByUserName(userName);
		
		if(existing != null && existing.size() > 0)
			throw new RegistrationException("Customer account already exists: "+ userName);
		
		user.setCustomer(customer);
		user.setUserName(userName);
		user.setRole(Role.ROLE_CUSTOMER);
		user.setStatus(Status.ACTIVE);
		user.setToken(refreshToken(user));
		
		user = userRepository.save(user);
		return user;
	}
	
	/**
	 * Register System Admin
	 * 
	 * @return
	 */
	public Users registerAdmin() {
		List<Users> existing = userRepository.findByRole(Role.ROLE_ADMIN);
		
		if(existing != null && existing.size() > 0) {
			logger.info("Admin account already exists...");
			return existing.get(0);
		}
		
		Users user = new Users();
		user.setUserName(ADMIN_USERNAME);
		user.setPassword(ADMIN_PASSWORD);
		user.setRole(Role.ROLE_ADMIN);
		user.setStatus(Status.ACTIVE);
		user.setToken(refreshToken(user));
		
		return userRepository.save(user);
	}
	
	public Users resetToken(Users user) {
		Users original = userRepository.findById(user.getId()).get();
		original.setToken(refreshToken(original));
		return userRepository.save(original);
	}
	
	private String refreshToken(Users user) {
		// Generate JWT Token for API requests
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("role", user.getRole().name());
		
		String token = jwtUtility.generateToken(extras, user.getUserName(), TOKEN_VALIDITY);
		logger.info("Refereshed JWT Token: "+ token);
		return token;
	}
	
	private Customer createOrGetCustomer(Customer customer) {
		if(customer == null)
			throw new RegistrationException("Missing Customer details");
		
		if(customer.getId() != null) {
			// Existing Customer
			return customerRepository.findById(customer.getId()).get();
		}
		return customerService.save(customer);
	}
}
