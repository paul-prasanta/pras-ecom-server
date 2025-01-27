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

import java.util.Date;

import com.pras.account.Customer;
import com.pras.account.user.config.CryptoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Table(name = "user")
@Entity
public class Users {

	public static enum Role {
		ROLE_ADMIN, ROLE_CUSTOMER;
	}
	
	public static enum Status {
		PENDING, ACTIVE, DISABLE;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(name = "user_name", unique = true)
	String userName;
	
	@Convert(converter = CryptoConverter.class)
	String password;
	
	private String token;
	
	@Column(name = "role")
	@Enumerated(EnumType.ORDINAL)
	Role role;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private Status status = Status.PENDING; // Verification pending
	
	private Date updateTime;
	private Date creationTime;
	
	
	@PrePersist
	public void onCreate() {
		Date now = new Date();
		if(this.getCreationTime() == null)
			setCreationTime(now);
		if(this.getUpdateTime() == null)
			setUpdateTime(now);
	}
	
	@PreUpdate
	public void onUpdate() {
		setUpdateTime(new Date());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	@Override
	public String toString() {
		return "Users [id=" + id + ", userName=" + userName + ", password=" + password + ", role=" + role
				+ ", customer=" + customer + ", status=" + status + ", updateTime=" + updateTime + ", creationTime="
				+ creationTime + "]";
	}
}
