package com.vent.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vent.dto.BookingDTO;
import com.vent.dto.LongLatDTO;
import com.vent.dto.PaymentDTO;
import com.vent.dto.ReviewDTO;
import com.vent.exception.VentException;
import com.vent.service.VehicleRegisService;

@EnableScheduling
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleRegisAPI {
	
	@Autowired
	private VehicleRegisService vehicleRegisService;
	
	//TO get all bookings by userId
	@GetMapping("/bookings/{id}")
	public ResponseEntity<List<BookingDTO>> getBookingsById(@PathVariable Integer id) throws VentException{
		
		List<BookingDTO> list = vehicleRegisService.getBookingsById(id);
		
		return new ResponseEntity<List<BookingDTO>>(list,HttpStatus.OK);
	}
	
	
	@GetMapping("/bookings/byuser/{username}")
	public ResponseEntity<List<BookingDTO>> getBookingsByUsername(@PathVariable String username) throws VentException{
		
		List<BookingDTO> list = vehicleRegisService.getBookingsByUsername(username);
		
		return new ResponseEntity<List<BookingDTO>>(list,HttpStatus.OK);
	}
	
	//To do new booking for userId
	@PostMapping("/bookings/book/{id}")
	public ResponseEntity<Integer> bookVehicle(@RequestBody BookingDTO bookingDTO, @PathVariable Integer id) throws VentException{
		
		//bookingDTO contains: vehicleName, count, source, destination, booking date 
		
		Integer bid = vehicleRegisService.bookVehicle(id, bookingDTO);
		
		return new ResponseEntity<Integer>(bid,HttpStatus.OK);
	}
	
	//To do payment for bookingId
	@PostMapping("/bookings/payment/{id}/{bid}")
	public ResponseEntity<Integer> makePayment(@RequestBody PaymentDTO paymentDTO,@PathVariable Integer id,@PathVariable Integer bid) throws VentException{
		Integer pid=vehicleRegisService.makePayment(id,bid,paymentDTO);
		return new ResponseEntity<Integer>(pid,HttpStatus.OK);
	}
	
	//cancel booking for bookingId
	@DeleteMapping("/bookings/cancel/{id}/{bid}")
	public ResponseEntity<String> cancelBooking(@PathVariable Integer id,@PathVariable Integer bid) throws VentException{
		vehicleRegisService.cancelBooking(id,bid);
		String message="Booking with id: "+bid+" cancelled successfully!!";
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	
	//give review
	@PostMapping("/bookings/review/{id}/{bid}/{rating}")
	public ResponseEntity<String> giveReview(@PathVariable Integer id,@PathVariable Integer bid, @PathVariable Integer rating) throws VentException{
		
		vehicleRegisService.giveReview(id,bid, rating);
		String message="Thankyou for giving "+rating+" out of 10 as feedback for bookingId: "+bid;
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	
	//view all reviews
	@GetMapping("/bookings/review/{bid}")
	public ResponseEntity<ReviewDTO> getReview(@PathVariable Integer bid) throws VentException{
		ReviewDTO reviewDTO = vehicleRegisService.getReview(bid);
		
		return new ResponseEntity<ReviewDTO>(reviewDTO,HttpStatus.OK);
	}
	
	//get latitude and longitude of source and destination city
	@GetMapping("/bookings/longlat/{scity}/{dcity}")
	public ResponseEntity<LongLatDTO> getLongLat(@PathVariable String scity,@PathVariable String dcity) throws VentException{
		LongLatDTO longLatDTO=vehicleRegisService.getLonglat(scity,dcity);
		return new ResponseEntity<LongLatDTO>(longLatDTO,HttpStatus.OK);
	}
	
	//get all cities available
	@GetMapping("/bookings/cities")
	public ResponseEntity<List<String>> getAllCities() throws VentException
	{
		List<String> citylist=vehicleRegisService.getAllCities();
		return new ResponseEntity<List<String>>(citylist,HttpStatus.OK);
	}
	
	//get amount corresponding to bookingId
	@GetMapping("/bookings/amount/{bid}")
	public ResponseEntity<Double> getBidAmount(@PathVariable Integer bid) throws VentException
	{
		Double amount=vehicleRegisService.getBidAmount(bid);
		return new ResponseEntity<Double>(amount,HttpStatus.OK);
	}
	
	// 1000 milliseconds = 1 second
	@Scheduled(fixedRate=86400000)
	public void greeting() throws VentException{
		vehicleRegisService.updateDB();
		System.out.println("Database updated Successfully!!");
	}
	
}
