package com.vent.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vent.entity.Vehicles;

@Repository(value="vehicleRepository")
public interface VehicleRepository extends CrudRepository<Vehicles,String> {

	public Vehicles findByVehicleType(String vehicleType);
	
}
