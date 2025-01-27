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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pras.account.user.config.LoginUtilityService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/users")
@RestController
@Tag(name = "User Accounts")
public class UserController {

	@Autowired
	LoginUtilityService loginUtilityService;
	@Autowired
	UserService userService;
	
	@GetMapping("/account")
	public Users getAccount() {
		return loginUtilityService.getLoggedInUser();
	}
	
	@PostMapping("/token_reset")
	public Users resetToken() {
		Users user = loginUtilityService.getLoggedInUser();
		return userService.resetToken(user);
	}
}
