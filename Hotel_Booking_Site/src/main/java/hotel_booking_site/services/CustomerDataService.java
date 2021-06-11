package hotel_booking_site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotel_booking_site.domain.Customer;
import hotel_booking_site.repository.CustomersRepository;

@Service
public class CustomerDataService {
	
	@Autowired 
	CustomersRepository customersRepository;
	
	public boolean persistNewCustomer(Customer customer) {
		customersRepository.save(customer);
		return true;
	}
	
	public Customer authenticateCustomer(String username, String password) {
		
		Customer customer = customersRepository.authenticateCustomer(username, password);	
		if (customer == null) {
			return null;
		}
		else {
		return customer;
		}
	}
	
	public int findCustomerIdByUsername(String username) {
		Customer customer = customersRepository.findCustomerIdByUsername(username);		
		return customer.getId();
	}
	
	public Customer findCustomerById(int id) {
		Customer customer = customersRepository.findbyCustomerId(id);
		return customer;
	}
}
