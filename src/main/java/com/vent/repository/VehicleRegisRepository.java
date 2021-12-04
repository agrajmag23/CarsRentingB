package com.vent.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vent.entity.Booking;

@Repository(value="vehicleRegisRepository")
public interface VehicleRegisRepository extends CrudRepository<Booking, Integer>{
	
	@Query("select b from Booking b where bid=?1 and u_id=?2")
	public Optional<Booking> findByIdAndUId(Integer bid,Integer uid);
	
	@Query("select b from Booking b where b.status=status")
	public List<Optional<Booking>> findByStatus(String status);
}
