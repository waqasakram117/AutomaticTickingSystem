package com.delivery.system.customer.services;

import com.delivery.system.customer.entities.Customer;
import com.delivery.system.customer.repos.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	private final CustomerRepo repo;

	@Autowired
	public CustomerService(CustomerRepo repo) {
		this.repo = repo;
	}

	public void addNewCustomer(String name) {
		repo.save(Customer.of(name));
	}

	public void incrementTotalOrders(Long customerId) {
		repo.findById(customerId);
	}

	private Customer getCustomerById(Long customerId) {
		return repo.findById(customerId).get();
	}

}
