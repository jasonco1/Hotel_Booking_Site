package hotel_booking_site;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import hotel_booking_site.domain.*;
import hotel_booking_site.repository.*;
import hotel_booking_site.services.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class NewBookingServiceTest {

	@MockBean 
	BookingsRepository bookingsRepository;
	
	@MockBean 
	PackageBookingsRepository packageBookingsRepository;
	
	@MockBean
	RoomsRepository roomsRepository;
	
	@MockBean
	HotelsRepository hotelsRepository;
	
	@Autowired
	NewBookingService newBookingService;
	
	@BeforeEach
	public void setUpEach(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void newBookingServiceShouldPersistNewBooking() {
	
	Booking booking = new Booking(1, 1, 1, 199.0, "6/1/2021", "6/03/2021", 2);
	given(bookingsRepository.save(booking)).willReturn(booking);
	assertThat(true).isEqualTo(newBookingService.persistNewBooking(booking));
	}
	
	@Test
	public void newBookingServiceShouldPersistPackageBooking() {
	
	PackageBooking packageBooking = new PackageBooking(1, 1, 1, 199.0, "6/1/2021", "6/03/2021", 2);
	given(packageBookingsRepository.save(packageBooking)).willReturn(packageBooking);
	assertThat(true).isEqualTo(newBookingService.persistNewPackageBooking(packageBooking));
	}
	
	@Test
	public void shouldReturnListOfCustomerBookings() {
		
		//Build test data
		Hotel hotel = new Hotel(1, "Hotel", "111 Avenue", "Monterey", "CA", 
				"USA", "10001", "777-777-7777", 5, "<img>", 5, "Pool", "Bay");
		Room room = new Room(1, 1, 199.0, 1, "Single", 1);
		
		Booking booking1 = new Booking(1, 1, 1, 199.0, "6/1/2021", "6/03/2021", 2);
		Booking booking2 = new Booking(2, 1, 1, 199.0, "6/1/2021", "6/03/2021", 2);
		List<Booking> bookings = new ArrayList<>();
		List<Booking> emptyList = new ArrayList<>();
		bookings.add(booking1);
		bookings.add(booking2);
		
		BookingInfo bookingInfo1 = new BookingInfo(booking1, room, hotel);
		BookingInfo bookingInfo2 = new BookingInfo(booking2, room, hotel);
		List<BookingInfo> expectedResult = new ArrayList<>();
		expectedResult.add(bookingInfo1);
		expectedResult.add(bookingInfo2);
		
		//Test stubs for valid search 
		given(bookingsRepository.findBookingsByCustomerId(1)).willReturn(bookings);
		given(roomsRepository.findbyRoomId(1)).willReturn(room);
		given(hotelsRepository.findById(1)).willReturn(hotel);
		
		//Test stubs for invalid search
		given(bookingsRepository.findBookingsByCustomerId(2)).willReturn(emptyList);
		
		//Compare expected and actual result
		List<BookingInfo> actualResult = newBookingService.getListOfBookingsByCustomerId(1);
		List<BookingInfo> noResults = newBookingService.getListOfBookingsByCustomerId(2);
		
		assertEquals(expectedResult, actualResult);
		assertEquals(null, noResults);
	}
	
	@Test
	public void cancelBookingShouldReturnNumberOfRowsDeleted() {
		
		//Stubs for valid and invalid booking ids
		given(bookingsRepository.deleteByBookingId(1)).willReturn(1);
		given(bookingsRepository.deleteByBookingId(0)).willReturn(0);
		
		int actualResult = newBookingService.cancelHotelBooking(1);
		int actualResultInvalidId = newBookingService.cancelHotelBooking(0);
		
		assertEquals(1, actualResult);
		assertEquals(0, actualResultInvalidId);
	}
	
	@Test
	public void cancelPackageBookingShouldReturnRowsDeleted() {
		
		//Stubs for valid and invalid booking ids
		given(packageBookingsRepository.deletePackageBookingById(1)).willReturn(1);
		given(packageBookingsRepository.deletePackageBookingById(0)).willReturn(0);
		
		int actualResult = newBookingService.cancelPackageHotelBooking(1);
		int actualResultInvalidId = newBookingService.cancelPackageHotelBooking(0);
		
		assertEquals(1, actualResult);
		assertEquals(0, actualResultInvalidId);
	}
}
