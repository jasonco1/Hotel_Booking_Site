package hotel_booking_site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hotel_booking_site.domain.PackageBooking;

public interface PackageBookingsRepository extends JpaRepository<PackageBooking, Long> {

	@Modifying
	@Transactional
	@Query(value="DELETE FROM package_bookings WHERE id = ?1", nativeQuery=true)  
	int deletePackageBookingById(int id); 
}
