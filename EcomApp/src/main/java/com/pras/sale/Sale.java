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

import java.util.Date;
import java.util.List;

import com.pras.account.Customer;

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
import jakarta.persistence.Transient;

@Table(name = "sale")
@Entity
public class Sale {

	public enum Status {
		Pending, Complete, Cancel; 
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	private Double totalPrice;
	private Integer totalCount;
	
	@Enumerated(EnumType.ORDINAL)
	private Status status = Status.Pending;
	
	private String note;
	
	private Date updateTime;
	private Date creationTime;
	
	@Transient
	private List<SaleItem> items;
	
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
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<SaleItem> getItems() {
		return items;
	}
	public void setItems(List<SaleItem> orderItems) {
		this.items = orderItems;
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
		return "Order [id=" + id + ", customer=" + customer + ", totalPrice=" + totalPrice + ", totalCount="
				+ totalCount + ", status=" + status + ", note=" + note + ", updateTime=" + updateTime
				+ ", creationTime=" + creationTime + ", orderItems=" + items + "]";
	}
}
