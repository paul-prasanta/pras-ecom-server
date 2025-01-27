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

package com.pras.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pras.account.user.config.LoginUtilityService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/customers")
@RestController
@Tag(name = "Customers")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	@Autowired
	LoginUtilityService loginUtilityService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("")
	public Customer save(@RequestBody Customer customer, @RequestParam(name = "id", required = false) Integer id) {
		customer.setId(id);
		return customerService.save(customer);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("")
	public Customer get(@RequestParam("id") Integer id) {
		return customerService.get(id);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/all")
	public List<Customer> getAll() {
		return customerService.getAll();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
	@GetMapping("/profile")
	public Customer getProfile() {
		return loginUtilityService.getLoggedInUser().getCustomer();
	}
}
