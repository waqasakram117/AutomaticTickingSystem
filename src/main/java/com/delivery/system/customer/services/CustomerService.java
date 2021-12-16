package com.delivery.system.customer.services;

import com.delivery.system.customer.entities.Customer;
import com.delivery.system.customer.repos.CustomerRepo;
import com.delivery.system.exceptions.NotFoundException;
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
		var customer = getCustomerById(customerId);
		customer.setTotalOrdersCount(customer.getTotalOrdersCount() + 1);
		repo.saveAndFlush(customer);
	}

	private Customer getCustomerById(Long customerId) {

		return repo.findById(customerId).orElseThrow(() -> new NotFoundException("Customer doesn't exist against Id: "+customerId));
	}

}
