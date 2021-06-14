package hotel_booking_site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hotel_booking_site.domain.Customer;

import org.springframework.data.jpa.repository.Query;

public interface CustomersRepository extends JpaRepository<Customer, Long> {
	
	@Query(value="SELECT c.id, c.first_name, c.last_name, c.email, c.password, c.current_balance FROM customers c WHERE c.email = ?1 LIMIT 1", nativeQuery=true)
	Customer getCustomer(String username);
	
	@Query(value="SELECT c.id, c.first_name, c.last_name, c.email, c.password, c.current_balance FROM customers c WHERE c.email = ?1 LIMIT 1", nativeQuery=true)
	Customer findCustomerIdByUsername(String username);
	
	@Query(value="SELECT c.id, c.first_name, c.last_name, c.email, c.password, c.current_balance FROM customers c WHERE c.id = ?1 LIMIT 1", nativeQuery=true)
	Customer findbyCustomerId(int id);
	
	@Query(value="SELECT COALESCE(SUM(b.total_price), 0) FROM customers c JOIN bookings b on c.id = b.customer_id WHERE c.id = ?1", nativeQuery=true)
	double returnAccountBalanceById(int id);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE customers SET current_balance = ?2 WHERE id = ?1", nativeQuery=true)
	void updateAccountBalance(int id, double balance);
}
