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

package com.pras.catalogue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * Product catalogue management.
 * 
 * Caching strategy:
 * - ID wise entries: Cache content is created or updated during Product creation / update / query 
 * - Category wise and All entries: Cache content is created during search query and evicted if any product is created or updated
 * 
 * @author Prasanta Paul
 */
@CacheConfig(cacheNames={"products"})
@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	CacheManager cacheManager;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Caching( // Evict list and update individual values
		evict = {
			@CacheEvict(key = "{'search', #product.category}"),
			@CacheEvict(key = "'all'")
		},
		put = {
			@CachePut(key = "{'getProduct', #product.id}")
		}
	)
	public Product create(Product product) {
		logger.info("Create Product: "+ product);
		return productRepository.save(product);
	}
	
	@Caching( // Evict list and update individual values
		evict = {
			@CacheEvict(key = "{'search', #product.category}"),
			@CacheEvict(key = "'all'")
		},
		put = {
			@CachePut(key = "{'getProduct', #product.id}")
		}
	)
	public Product update(Product product) {
		logger.info("Update Product: "+ product);
		Product original = productRepository.findById(product.getId()).get();
		original.setName(product.getName());
		original.setBrand(product.getBrand());
		original.setDescription(product.getDescription());
		original.setPrice(product.getPrice());
		return productRepository.save(original);
	}
	
	@Cacheable(key = "{#root.methodName, #id}")
	public Product getProduct(Integer id) {
		logger.info("Get Product By ID: {}", id);
		logger.info("Cache: "+ cacheManager.getCache("products").getNativeCache());
		return productRepository.findById(id).get();
	}
	
	@Cacheable(key = "{#root.methodName, #category}") // Avoid null parameter
	public List<Product> search(String category) {
		logger.info("Get Products By Category: {}", category);
		return productRepository.findByCategory(category);
	}
	
	@Cacheable(key = "'all'") // Avoid null parameter
	public List<Product> searchAll() {
		logger.info("Get All Products...");
		return productRepository.findAll();
	}
}
