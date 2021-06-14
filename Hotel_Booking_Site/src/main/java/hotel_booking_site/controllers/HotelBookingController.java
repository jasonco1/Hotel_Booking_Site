package hotel_booking_site.controllers;
/* References
 * https://stackoverflow.com/questions/10413350/date-conversion-from-string-to-sql-date-in-java-giving-different-output/17673514
 * 
 */
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hotel_booking_site.domain.Booking;
import hotel_booking_site.domain.BookingInfo;
import hotel_booking_site.domain.Customer;
import hotel_booking_site.domain.RoomInfo;
import hotel_booking_site.services.AvailableRoomsService;
import hotel_booking_site.services.CustomerDataService;
import hotel_booking_site.services.NewBookingService;

@Controller
public class HotelBookingController {
	
	//Convert this to use HTTP Sessions
	String persistedCity;
	int persistedRoomId;
	int persistedNumberOccupants;
	java.sql.Date sqlCheckInDate;
	java.sql.Date sqlCheckOutDate;
	
	@Autowired 
	AvailableRoomsService availableRoomsService;
	
	@Autowired
	NewBookingService newBookingService;
	
	@Autowired
	CustomerDataService customerDataService;
	
	//HTTP Routes
	@GetMapping("/hotels/home")
	public String getHotelsHomepage(Model model) {
		return "hotels_homepage";
	}
	
	@PostMapping("/hotels/results")
	public String getHotelSearchResults(Model model,
			@RequestParam("city") String city,
			@RequestParam("checkInDate") String checkInDate,
			@RequestParam("checkOutDate") String checkOutDate,
			@RequestParam("occupants") int numberOccupants
			) throws ParseException {
		
		//persist city and checkIn/Out dates
		persistedCity = city;
		sqlCheckInDate = newBookingService.stringToSqlDate(checkInDate);
		sqlCheckOutDate = newBookingService.stringToSqlDate(checkOutDate);
		persistedNumberOccupants = numberOccupants;
		
		model.addAttribute("city", persistedCity);
		model.addAttribute("checkInDate", sqlCheckInDate);
		model.addAttribute("checkOutDate", sqlCheckOutDate);
		model.addAttribute("numberOccupants", persistedNumberOccupants);
		
		//Query database to find available rooms
		List<RoomInfo> roomInfoList = availableRoomsService.getAvailableRooms(city, sqlCheckInDate, sqlCheckOutDate);
		
		if (roomInfoList == null) {
			String no_rooms_found = "Sorry, no available rooms where found. Please try "
					+ "a different city or date.";
			model.addAttribute("no_results", true);
			model.addAttribute("no_rooms_found", no_rooms_found);
			return "hotels_homepage";
		}
		
		else {
		model.addAttribute(roomInfoList);
		return "hotel_results_page";
		}
	}
	
	@PostMapping("/hotels/checkout")
	public String getCheckoutDetails(Model model,
			@RequestParam("roomId") int roomId
			) {
		persistedRoomId = roomId;
		
		model.addAttribute("roomId", persistedRoomId);
		model.addAttribute("city", persistedCity);
		model.addAttribute("checkInDate", sqlCheckInDate);
		model.addAttribute("checkOutDate", sqlCheckOutDate);
		
		return "new_customer_checkout_page";
	}
	
	@PostMapping("/hotels/submitNewCustomerBooking")
	public String persistBookingAndCustomerToDatabase(Model model,
			//new booking parameters
			@RequestParam("roomId") int roomId,
			@RequestParam("numberOccupants") int numberOccupants,
			
			//new customer parameters
			@RequestParam("firstName") String first_name,
			@RequestParam("lastName") String last_name,
			@RequestParam("email") String email,
			@RequestParam("password") String password
			) {
		
		model.addAttribute("checkInDate", sqlCheckInDate);
		model.addAttribute("checkOutDate", sqlCheckOutDate);
		model.addAttribute("totalPrice", 199.00);
		model.addAttribute("numberOccupants", persistedNumberOccupants);
		
		//persist new customer to database
		Customer customer = new Customer();
		customer.setFirst_name(first_name);
		customer.setLast_name(last_name);
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setCurrent_balance(150);
		customerDataService.persistNewCustomer(customer);
		
		//persist new booking to database
		Booking booking = new Booking();
		booking.setRoom_id(persistedRoomId);
		booking.setCustomer_id(customerDataService.findCustomerIdByUsername(email));
		booking.setCheck_in_date(sqlCheckInDate);
		booking.setCheck_out_date(sqlCheckOutDate);
		booking.setTotal_price(199.00);
		booking.setNumber_occupants(1);	
		newBookingService.persistNewBooking(booking);
		
		model.addAttribute(customer);
		
		return "booking_confirmation_page";
	}
	
	@PostMapping("/hotels/myAccount")
	public String getCustomerAccountDetails(Model model,
			@RequestParam("username") String username,
			@RequestParam("password") String password
			) {
		
		Customer customer =	customerDataService.authenticateCustomer(username, password);
		if (customer == null) {
			model.addAttribute("invalid_login", true);
			return "customer_login_page";
		}
		else {
		model.addAttribute(customer);
		
		//Build list of customer's bookings if login is valid
		List<BookingInfo> bookingInfoList = newBookingService.getListOfBookingsByCustomerId(customer.getId());
		if (bookingInfoList == null) {
			model.addAttribute("bookings", false);
		}
		
		else {
		model.addAttribute("bookings", true);
		model.addAttribute(bookingInfoList);
		}
		
		return "customer_account_page";
		}
	}
	
	@PostMapping("/hotels/cancelHotelBooking")
	public String cancelHotelBooking(Model model, 
			@RequestParam("booking_id") int booking_id,
			@RequestParam("customer_id") int customer_id
			){
		
		//cancel hotel booking
		newBookingService.cancelHotelBooking(booking_id);
		
		//retrieved updated list of customer bookings
		Customer customer = customerDataService.findCustomerById(customer_id);
		model.addAttribute(customer);
		
		List<BookingInfo> bookingInfoList = newBookingService.getListOfBookingsByCustomerId(customer.getId());
		if (bookingInfoList == null) {
			model.addAttribute("bookings", false);
		}
		
		else {
		model.addAttribute("bookings", true);
		model.addAttribute(bookingInfoList);
		}
		
		return "customer_account_page";
	}
	
	//Navigation Bar Routes
	@GetMapping("/hotels/login")
	public String getHotelsLoginPage(Model model) {
		return "customer_login_page";
	}
	
	@GetMapping("/hotels/signup")
	public String getHotelsSignUpPage(Model model) {
		return "hotels_signup_page";
	}
	
	@GetMapping("/airline")
	public String getAirlineHomepage(Model model) {
		return "airline";
	}
	
	@GetMapping("/carrentals")
	public String getCarRentalsHomepage(Model model) {
		return "car_rentals";
	}
	
	@GetMapping("/deals")
	public String getDealseHomepage(Model model) {
		return "deals";
	}
		
}
