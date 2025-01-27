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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.pras.account.Customer;
import com.pras.account.CustomerRepository;
import com.pras.catalogue.Product;
import com.pras.catalogue.ProductRepository;
import com.pras.sale.Sale.Status;
import com.pras.sale.dto.DtoConverter;
import com.pras.sale.dto.SaleDto;
import com.pras.utils.Exceptions.OperationException;

/**
 * Sales / Order management
 * 
 * Caching strategy:
 * - ID wise entries: Cache content is created or updated during Order creation / update / query 
 * - Customer wise and All entries: Cache content is created during search query and evicted if any order is created or updated
 * 
 * @author Prasanta Paul
 */
@CacheConfig(cacheNames={"sales"})
@Service
public class SaleService {

	@Autowired
	private SaleRepository saleRepository;
	@Autowired
	private SaleItemRepository saleItemRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Create Sale entry. It remains in Pending status which can be further updated (by Admin) after validation and fulfillment
	 * 
	 * @param sale
	 * @return
	 */
	@Caching( // Evict list and update individual values
		evict = {
			@CacheEvict(key = "{'getSalesByCustomer', #sale.customer.id}"),
			@CacheEvict(key = "'all'")
		},
		put = {
			@CachePut(key = "{'getSale', #sale.id}")
		}
	)
	public SaleDto create(Sale sale) {
		logger.info("Create order: {}", sale);
		
		// Read Order Items
		List<SaleItem> items = sale.getItems();
		
		if(items == null || items.size() == 0) {
			throw new OperationException("Missing items");
		}
		
		Integer totalCount = 0;
		Double totalPrice = 0.0;
		for(SaleItem i : items) {
			Product product = productRepository.findById(i.getProduct().getId()).get();
			i.setProduct(product);
			
			// Calculate item count and price
			if(i.getCount() != null) {
				totalCount += i.getCount();
				Double price = product.getPrice() * i.getCount();
				i.setPrice(price);
				totalPrice += price;
			}
		}
		
		sale.setStatus(Status.Pending);
		sale.setTotalCount(totalCount);
		sale.setTotalPrice(totalPrice);
		
		sale = saleRepository.save(sale);
		
		// Save Sale Items
		for(SaleItem i : items) {
			i.setSale(sale);
		}
		
		saleItemRepository.saveAll(items);
		return convertToDto(sale);
	}
	
	@Caching( // Evict list and update individual values
		evict = {
			@CacheEvict(key = "{'getSalesByCustomer', #sale.customer.id}"),
			@CacheEvict(key = "'all'")
		},
		put = {
			@CachePut(key = "{'getSale', #sale.id}")
		}
	)
	public SaleDto update(Sale sale, Status status) {
		logger.info("Update Sale {}/{}", sale, status);
		if(sale.getStatus() != Status.Pending) {
			throw new OperationException("Can't update non-pending sales entry");
		}
		
		sale.setStatus(status);
		sale = saleRepository.save(sale);
		return convertToDto(sale);
	}
	
	@Cacheable(key = "{#root.methodName, #id}")
	public SaleDto getSale(Integer id) {
		logger.info("Get sales entry : {}", id);
		Sale sale = saleRepository.findById(id).get();
		return convertToDto(sale);
	}
	
	@Cacheable(key = "{#root.methodName, #customerId}")
	public List<SaleDto> getSalesByCustomer(Integer customerId) {
		logger.info("Get sales by customer : {}", customerId);
		Customer customer = customerRepository.findById(customerId).get();
		List<Sale> sales = saleRepository.findByCustomer(customer);
		return convertToDto(sales);
	}
	
	@Cacheable(key = "'all'")
	public List<SaleDto> getAllSales() {
		logger.info("Get all sales entry...");
		List<Sale> sales = saleRepository.findAll();
		return convertToDto(sales);
	}
	
	private List<SaleDto> convertToDto(List<Sale> sales) {
		List<SaleDto> dtos = new ArrayList<>();
		for(Sale sale : sales)
			dtos.add(convertToDto(sale));
		return dtos;
	}
	
	private SaleDto convertToDto(Sale sale) {
		List<SaleItem> saleItems = saleItemRepository.findBySale(sale); 
		return new DtoConverter().convert(sale, saleItems);
	}
}
