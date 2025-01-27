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
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	public Customer save(Customer customer) {
		if(customer.getId() == null) {
			// Create
			return customerRepository.save(customer);
		}
		
		// Update
		Customer original = customerRepository.findById(customer.getId()).get();
		original.setName(customer.getName());
		original.setGender(customer.getGender());
		original.setEmail(customer.getEmail());
		original.setMobileNumber(customer.getMobileNumber());
		
		return customerRepository.save(original);
	}
	
	public Customer get(Integer id) {
		return customerRepository.findById(id).get();
	}
	
	public List<Customer> getAll() {
		return customerRepository.findAll();
	}
}
