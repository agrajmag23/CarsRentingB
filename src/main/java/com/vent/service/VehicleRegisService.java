package com.vent.service;

import java.util.List;

import com.vent.dto.BookingDTO;
import com.vent.dto.LongLatDTO;
import com.vent.dto.PaymentDTO;
import com.vent.dto.ReviewDTO;
import com.vent.exception.VentException;


public interface VehicleRegisService {
	
	public Integer bookVehicle(Integer userId, BookingDTO bookingDTO) throws VentException;
	
	
	public List<BookingDTO> getBookingsById(Integer userId) throws VentException;
	
	public List<BookingDTO> getBookingsByUsername(String username) throws VentException;
	
	public Integer makePayment(Integer id,Integer bid, PaymentDTO paymentDTO) throws VentException;
	
	public Integer giveReview(Integer uid,Integer bid, Integer rating) throws VentException;
	
	public ReviewDTO getReview(Integer bid) throws VentException;
    
    public void cancelBooking(Integer id,Integer bid) throws VentException;
    
    public LongLatDTO getLonglat(String scity,String dcity) throws VentException;
    
    public List<String> getAllCities() throws VentException;
    
    public Double getBidAmount(Integer bid) throws VentException;
    
    public void updateDB() throws VentException;
}
