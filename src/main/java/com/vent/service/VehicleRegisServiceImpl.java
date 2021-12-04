package com.vent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.vent.entity.Booking;
import com.vent.entity.Payment;
import com.vent.entity.Review;
import com.vent.entity.User;
import com.vent.exception.VentException;
import com.vent.dto.BookingDTO;
import com.vent.dto.LongLatDTO;
import com.vent.dto.PaymentDTO;
import com.vent.dto.ReviewDTO;
import com.vent.dto.VehicleDTO;
import com.vent.repository.CitiesRepository;
import com.vent.repository.LoginRepository;
import com.vent.repository.PaymentRepository;
import com.vent.repository.ReviewRepository;
import com.vent.repository.VehicleRegisRepository;



@Service(value="VehicleRegisService")
public class VehicleRegisServiceImpl implements VehicleRegisService{
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private  VehicleRegisRepository vehicleRegisRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private CitiesRepository citiesRepository;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private Environment environment;
	
	
	@Override
	public List<BookingDTO> getBookingsById(Integer userId) throws VentException{
		//Checking whether user exist or not
		Optional<User> userOpt = loginRepository.findById(userId);
		if(userOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.USER_DOESN'T_EXIST"));
		//if user exists
		User user = userOpt.get();
		List<Booking> list = user.getBookings();
		
		List<BookingDTO> list2 = new ArrayList<BookingDTO>();
		
		for(int i = 0 ; i < list.size(); i++) {
			BookingDTO bookingDTO = new BookingDTO();
			Booking booking = list.get(i);
			bookingDTO.setBid(booking.getBid());
			bookingDTO.setVehicleName(booking.getVehicleName());
			bookingDTO.setVehicleCount(booking.getVehicleCount());
			bookingDTO.setDateOfRegistration(booking.getDateOfRegistration());
			bookingDTO.setBookingDate(booking.getBookingDate());
			bookingDTO.setSource(booking.getSource());
			bookingDTO.setDestination(booking.getDestination());
			bookingDTO.setDuration(booking.getDuration());
			bookingDTO.setDistance(booking.getDistance());
			bookingDTO.setAmount(booking.getAmount());
			bookingDTO.setPayment(booking.getPayment());
			bookingDTO.setReview(booking.getReview());
			bookingDTO.setStatus(booking.getStatus());
			list2.add(bookingDTO);
		}
		return list2;
	}
	
	
	@Override
	public List<BookingDTO> getBookingsByUsername(String username) throws VentException {
		// TODO Auto-generated method stub
		Optional<User> userOpt=loginRepository.findByuserName(username);
		if(userOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.USER_DOESN'T_EXIST"));
		User user=userOpt.get();
		return getBookingsById(user.getId());
	}
	
	
	@Override
	public Integer bookVehicle(Integer userId, BookingDTO bookingDTO) throws VentException{
		//Checking whether user exist or not
		Optional<User> userOpt = loginRepository.findById(userId);
		if(userOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.USER_DOESN'T_EXIST"));
		//if user exists
		User user = userOpt.get();
		
		//booking logic starts here
		List<Booking> list = user.getBookings();
		
		Booking booking = new Booking();
		booking.setVehicleName(bookingDTO.getVehicleName());
		booking.setVehicleCount(bookingDTO.getVehicleCount());
		booking.setDateOfRegistration(LocalDate.now());
		booking.setBookingDate(bookingDTO.getBookingDate());
		
		if(booking.getBookingDate().isBefore(booking.getDateOfRegistration()))
			throw new VentException(environment.getProperty("Service.INVALID_JOURNEY_DATE"));
		
		booking.setSource(bookingDTO.getSource());
		booking.setDestination(bookingDTO.getDestination());
		booking.setDuration(bookingDTO.getDuration());
		LongLatDTO longLatDTO=getLonglat(booking.getSource(),booking.getDestination());
		Double distance=Double.parseDouble(String.format("%.2f",calculateDistance(longLatDTO.getSourceLongitude(),longLatDTO.getSourceLatitude(),longLatDTO.getDestLongitude(),longLatDTO.getDestLatitude())));
		Double amount=Double.parseDouble(String.format("%.2f",CalculateFare(distance,booking.getVehicleCount())));
		
		booking.setDistance(distance);
		booking.setAmount(amount);
		booking.setStatus("payment pending");
		//determine whether required capacity available or not. Decrementing bookingCount if that much count available
		vehicleService.decVehicleTypeCount(bookingDTO.getVehicleName(), bookingDTO.getVehicleCount());
		Booking newBooking = vehicleRegisRepository.save(booking);
		
		list.add(newBooking);
		loginRepository.save(user);
		
		return newBooking.getBid();
	}
	
	@Override
	public Integer makePayment(Integer uid,Integer bid, PaymentDTO paymentDTO) throws VentException {
		// checking whether booking exists or not
		Optional<Booking> bookingOpt = vehicleRegisRepository.findByIdAndUId(bid,uid);
		if(bookingOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.Booking_DOESN'T_EXIST"));
	
		//if booking exists
		Booking booking = bookingOpt.get();
		
		if(!booking.getStatus().equalsIgnoreCase("payment pending"))
		{
			if(booking.getStatus().equalsIgnoreCase("cancelled"))
				throw new VentException(environment.getProperty("Service.Booking_CANCELLED"));
			if(booking.getStatus().equalsIgnoreCase("paid") || booking.getStatus().equalsIgnoreCase("rated"))
				throw new VentException(environment.getProperty("Service.Booking_PAYMENT_ALREADY_DONE"));
			if(booking.getStatus().equalsIgnoreCase("payment timeout"))
				throw new VentException(environment.getProperty("Service.Booking_AUTO_CANCELLED"));
		}
		
		Payment payment=new Payment();
		payment.setPayment_type(paymentDTO.getPayment_type());
		payment.setPayment_info(paymentDTO.getPayment_info());
		payment.setAmount(Double.parseDouble(String.format("%.2f",paymentDTO.getAmount())));
		payment.setPstatus("success");
		Payment paymentSaved=paymentRepository.save(payment);
		booking.setPayment(paymentSaved);
		booking.setStatus("paid");
		vehicleRegisRepository.save(booking);
		return paymentSaved.getPid();
	}
	
	@Override
	public Integer giveReview(Integer uid,Integer bid, Integer rating) throws VentException{
		Optional<Booking> bookingOpt = vehicleRegisRepository.findByIdAndUId(bid,uid);
		if(bookingOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.Booking_DOESN'T_EXIST"));
		Booking booking = bookingOpt.get();
		String statusValue=booking.getStatus();
		if(statusValue.equalsIgnoreCase("payment pending"))
			throw new VentException(environment.getProperty("Service.PAYMENT_NOT_DONE"));
		if(statusValue.equalsIgnoreCase("payment timeout"))
			throw new VentException(environment.getProperty("Service.Booking_AUTO_CANCELLED"));
		if(statusValue.equalsIgnoreCase("cancelled"))
			throw new VentException(environment.getProperty("Service.Booking_CANCELLED"));
		if(statusValue.equalsIgnoreCase("rated"))
			throw new VentException(environment.getProperty("Service.ALREADY_REVIEWED"));
		if(rating<0 || rating>10)
			throw new VentException(environment.getProperty("Service.INVALID_RATING"));
		
		Review review = new Review();
		review.setRating(rating);
		Review newReview = reviewRepository.save(review);
		booking.setReview(review);
		booking.setStatus("rated");
		vehicleRegisRepository.save(booking);
		 VehicleDTO vehicleDTO=new VehicleDTO();
	  	 vehicleDTO.setVehicleType(booking.getVehicleName());
	  	 vehicleDTO.setCount(booking.getVehicleCount());
	  	 vehicleService.updateVehicle(vehicleDTO);
		return newReview.getRid();
	}

	@Override
	public ReviewDTO getReview(Integer bid) throws VentException{
		Optional<Booking> bookingOpt = vehicleRegisRepository.findById(bid);
		if(bookingOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.Booking_DOESN'T_EXIST"));
		Booking booking = bookingOpt.get();
		if(!booking.getStatus().equalsIgnoreCase("rated"))
			throw new VentException(environment.getProperty("Service.REVIEW_DOESN'T_EXIST"));
		Optional<Review> reviewOpt = reviewRepository.findById(booking.getReview().getRid());
		if(reviewOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.REVIEW_DOESN'T_EXIST"));
		Review review = reviewOpt.get();
		ReviewDTO reviewDTO = new ReviewDTO();
		reviewDTO.setRid(review.getRid());
		reviewDTO.setRating(review.getRating());
		
		return reviewDTO;
	}

	@Override
	public void cancelBooking(Integer uid,Integer bid) throws VentException {
		Optional<Booking> bookingOpt = vehicleRegisRepository.findByIdAndUId(bid,uid);
		if(bookingOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.Booking_DOESN'T_EXIST"));
		Booking booking = bookingOpt.get();
		String statusValue=booking.getStatus();
		String vehicleType=booking.getVehicleName();
		Integer vehicleCount=booking.getVehicleCount();
	
      // booking already cancelled
       if(statusValue.equalsIgnoreCase("cancelled"))
    	   throw new VentException(environment.getProperty("Service.Booking_CANCELLED"));
       if(statusValue.equalsIgnoreCase("payment timeout"))
			throw new VentException(environment.getProperty("Service.Booking_AUTO_CANCELLED"));
       // journey completed ie. rated OR payment done
       if(statusValue.equalsIgnoreCase("rated") || booking.getPayment()!=null || statusValue.equalsIgnoreCase("paid"))
    	   throw new VentException(environment.getProperty("Service.CANCELLATION_NOT_POSSIBLE"));
       
       booking.setStatus("cancelled");
	   VehicleDTO vehicleDTO=new VehicleDTO();
  	   vehicleDTO.setVehicleType(vehicleType);
  	   vehicleDTO.setCount(vehicleCount);
  	   vehicleService.updateVehicle(vehicleDTO);
	   return;

	}


	@Override
	public LongLatDTO getLonglat(String scity,String dcity) throws VentException {
		
		if(scity.equalsIgnoreCase(dcity))
			throw new VentException(environment.getProperty("Service.SAME_SOURCE_DES"));
		
		String slonstr=citiesRepository.findLongitudeByCity(scity);
		String slatstr=citiesRepository.findLatitudeByCity(scity);
		String dlonstr=citiesRepository.findLongitudeByCity(dcity);
		String dlatstr=citiesRepository.findLatitudeByCity(dcity);
		
		if(slonstr.length()<=2 || slatstr.length()<=2 || slonstr.isEmpty() || slatstr.isEmpty())
			throw new VentException(environment.getProperty("Service.SOURCE_DOESN'T_EXIST"));
		if(dlonstr.length()<=2 || dlatstr.length()<=2 || dlonstr.isEmpty() || dlatstr.isEmpty())
			throw new VentException(environment.getProperty("Service.DESTINATION_DOESN'T_EXIST"));
		
		Double sourceLong=getDoubleValue(slonstr);
		Double sourceLat=getDoubleValue(slatstr);
		Double destLong=getDoubleValue(dlonstr);
		Double destLat=getDoubleValue(dlatstr);
   
		LongLatDTO longLatDTO=new LongLatDTO();
		longLatDTO.setSourceLatitude(sourceLat);
		longLatDTO.setSourceLongitude(sourceLong);
		longLatDTO.setDestLatitude(destLat);
		longLatDTO.setDestLongitude(destLong);
		
		return longLatDTO;
		
	}
	
	public Double getDoubleValue(String latlong)
	{
		String str[]=latlong.split(" ");
		Double d=Double.parseDouble(str[0]);
		return d;
	}


	@Override
	public List<String> getAllCities() throws VentException {
		List<String> resList=new ArrayList<>();
		List<List<String>> citylist=citiesRepository.findAllCity();
		for(int i=0;i<citylist.size();i++)
		{
			String temp=citylist.get(i).get(0)+", "+citylist.get(i).get(1);
			resList.add(temp);
		}
		return resList;
	}


	@Override
	public Double getBidAmount(Integer bid) throws VentException {
		
		Optional<Booking> bookingOpt = vehicleRegisRepository.findById(bid);
		if(bookingOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.Booking_DOESN'T_EXIST"));
		//if booking exists
		Booking booking = bookingOpt.get();
		
		return booking.getAmount();
	}

	public Double calculateDistance(double sourceLong,double sourceLat,double destLong,double destLat)
	{
		sourceLong = Math.toRadians(sourceLong);
		destLong = Math.toRadians(destLong);
		sourceLat = Math.toRadians(sourceLat);
		destLat = Math.toRadians(destLat);
		
		double dlon = destLong - sourceLong;
        double dlat = destLat - sourceLat;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(sourceLat) * Math.cos(destLat) * Math.pow(Math.sin(dlon / 2),2);
             
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;
        return(c * r);
	}
	
	public Double CalculateFare(Double distance, Integer vehicleCount)
	{
		//Rs. 3 per kilometer is distance is less or equal to 30km
		if(distance <= 30)
		{
			return vehicleCount*distance*3;
		}
		//Rs. 4 per kilometer is distance in range 30km to 100km
		if(distance >30 && distance<=100)
		{
			return vehicleCount*distance*4;
		}
		//Rs. 5 per kilometer is distance is above 100km
		return  vehicleCount*distance*5;
	}


	@Override
	public void updateDB() throws VentException{
		Iterable<Booking> bookingIter=vehicleRegisRepository.findAll();
		
		List<Booking> bookingList=new ArrayList<>();
		
		for(Booking b : bookingIter)
		{
			bookingList.add(b);
		}
		
		if(bookingList.size()==0)
			return;
		for(int i=0;i<bookingList.size();i++)
		{
			Booking booking=bookingList.get(i);

			if(booking.getStatus().equalsIgnoreCase("payment pending"))
			{
			   String vehicleType=booking.getVehicleName();
			   Integer vehicleCount=booking.getVehicleCount();
			   booking.setStatus("payment timeout");
			   vehicleRegisRepository.save(booking);
			   VehicleDTO vehicleDTO=new VehicleDTO();
		       vehicleDTO.setVehicleType(vehicleType);
		       vehicleDTO.setCount(vehicleCount);
			   vehicleService.updateVehicle(vehicleDTO);
			 }
		}
	}

}
