package hotel_booking_site.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotel_booking_site.domain.Booking;
import hotel_booking_site.domain.BookingInfo;
import hotel_booking_site.domain.Hotel;
import hotel_booking_site.domain.PackageBooking;
import hotel_booking_site.domain.Room;
import hotel_booking_site.repository.BookingsRepository;
import hotel_booking_site.repository.HotelsRepository;
import hotel_booking_site.repository.PackageBookingsRepository;
import hotel_booking_site.repository.RoomsRepository;

@Service
public class NewBookingService {
	
	@Autowired 
	BookingsRepository bookingsRepository;

	@Autowired 
	HotelsRepository hotelsRepository;
	
	@Autowired
	RoomsRepository roomsRepository;
	
	@Autowired 
	PackageBookingsRepository packageBookingsRepository;
	
	public boolean persistNewBooking(Booking booking) {
		bookingsRepository.save(booking);
		return true;
	}
	
	public boolean persistNewPackageBooking(PackageBooking packageBooking) {
		packageBookingsRepository.save(packageBooking);
		return true;
	}
	
	public List<BookingInfo> getListOfBookingsByCustomerId(int customer_id) {
		
		List<BookingInfo> bookingInfoList = new ArrayList<>();
		List<Booking> bookings = bookingsRepository.findBookingsByCustomerId(customer_id);
		
		if (bookings.size() < 1) {
			return null;
		}
		
		for (Booking booking : bookings) {
			
			Room room = roomsRepository.findbyRoomId(booking.getRoom_id());
			Hotel hotel = hotelsRepository.findById(room.getHotel_id());
			BookingInfo bookingInfo = new BookingInfo(booking, room, hotel);
			bookingInfoList.add(bookingInfo);
		
		}
		return bookingInfoList;
	}
	
	public int cancelHotelBooking(int id) {
		return bookingsRepository.deleteByBookingId(id);
	}
	
	public int cancelPackageHotelBooking(int id) {
		return packageBookingsRepository.deletePackageBookingById(id);
	}
	
}
