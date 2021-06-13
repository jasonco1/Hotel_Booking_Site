package hotel_booking_site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotel_booking_site.domain.Customer;

import org.springframework.data.jpa.repository.Query;

public interface CustomersRepository extends JpaRepository<Customer, Long> {
	
	@Query(value="SELECT c.id, c.first_name, c.last_name, c.email, c.password, c.current_balance FROM customers c WHERE c.email = ?1 LIMIT 1", nativeQuery=true)
	Customer getCustomer(String username);
	
	@Query(value="SELECT c.id, c.first_name, c.last_name, c.email, c.password, c.current_balance FROM customers c WHERE c.email = ?1 LIMIT 1", nativeQuery=true)
	Customer findCustomerIdByUsername(String username);
	
	@Query(value="SELECT c.id, c.first_name, c.last_name, c.email, c.password, c.current_balance FROM customers c WHERE c.id = ?1 LIMIT 1", nativeQuery=true)
	Customer findbyCustomerId(int id);
	
}
