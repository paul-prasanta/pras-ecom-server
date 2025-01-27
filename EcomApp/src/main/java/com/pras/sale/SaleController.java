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

package com.pras.sale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pras.account.user.Users;
import com.pras.account.user.config.LoginUtilityService;
import com.pras.sale.Sale.Status;
import com.pras.sale.dto.SaleDto;
import com.pras.utils.Exceptions.AccessException;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/sales")
@RestController
@Tag(name = "Sales")
public class SaleController {
	
	@Autowired
	private SaleService saleService;
	@Autowired
	LoginUtilityService loginUtilityService;
	
	@Autowired
	private SaleRepository saleRepository;

	@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
	@PostMapping("/create")
	public SaleDto create(@RequestBody Sale order) {
		Users user = loginUtilityService.getLoggedInUser();
		order.setCustomer(user.getCustomer());
		return saleService.create(order);
	}
	
	/**
	 * Update Sale instance status by Admin
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/update")
	public SaleDto update(@RequestParam("id") Integer id, @RequestParam("status") Status status) {
		Sale sale = saleRepository.findById(id).get();
		return saleService.update(sale, status);
	}
	
	@GetMapping("")
	public SaleDto getSale(@RequestParam("id") Integer id) {
		Users user = loginUtilityService.getLoggedInUser();
		SaleDto saleDto = saleService.getSale(id);
		
		if(user.getCustomer() != null 
				&& !user.getCustomer().getId().equals(saleDto.getCustomer().getId())) {
			throw new AccessException("Not Allowed");
		}
		return saleDto;
	}
	
	@GetMapping("/all")
	public List<SaleDto> getSales(@RequestParam(name = "customerId", required = false) Integer customerId) {
		Users user = loginUtilityService.getLoggedInUser();
		if(user.getCustomer() != null) {
			// For customer get their sales records
			customerId = user.getCustomer().getId();
		}
		
		if(customerId != null)
			return saleService.getSalesByCustomer(customerId);
		else
			return saleService.getAllSales();
	}
}
