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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/products")
@RestController
@Tag(name = "Products")
public class ProductController {
	
	@Autowired
	ProductService productService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("")
	public Product save(@RequestBody Product product, @RequestParam(name = "id", required = false) Integer id) {
		if(id == null)
			return productService.create(product);
		else {
			product.setId(id);
			return productService.update(product);
		}
	}
	
	@GetMapping("")
	public Product get(@RequestParam("id") Integer id) {
		return productService.getProduct(id);
	}
	
	@GetMapping("/search")
	public List<Product> get(@RequestParam(name = "category", required = false) String category) {
		if(category != null)
			return productService.search(category);
		else
			return productService.searchAll();
	}
}
