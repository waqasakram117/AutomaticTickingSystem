package com.delivery.system.customer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "customers")
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "INT UNSIGNED")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "total_orders")
	private Long totalOrdersCount;

	public static Customer of(String name) {
		var customer = new Customer();
		customer.name = name;

		return customer;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTotalOrdersCount() {
		return totalOrdersCount;
	}

	public void setTotalOrdersCount(Long totalOrdersCount) {
		this.totalOrdersCount = totalOrdersCount;
	}
}
