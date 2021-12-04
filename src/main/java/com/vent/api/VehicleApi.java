package com.vent.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vent.dto.VehicleDTO;
import com.vent.exception.VentException;
import com.vent.service.VehicleService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value="/vehicle-api")
public class VehicleApi {

	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private Environment environment;
	
	//Admin: Add a new Vehicle Type
	@PostMapping(value="/addVehicle")
	public ResponseEntity<String> addVehicle(@RequestBody VehicleDTO vehicleDTO) throws VentException
	{
		Integer count=vehicleService.addVehicle(vehicleDTO);
		String message=environment.getProperty("API.VEHICLE_ADDED_SUCCESSFULLY")+" "+vehicleDTO.getVehicleType()+" : "+count;
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}
	
	//Admin: Update Vehicle Count
    @PostMapping(value="/updateVehicle")
	public ResponseEntity<String> updateVehicle(@RequestBody VehicleDTO vehicleDTO) throws VentException
	{
		Integer count=vehicleService.updateVehicle(vehicleDTO);
		
		String message=environment.getProperty("API.VEHICLE_UPDATED_SUCCESSFULLY")+" ,new count : "+ count;
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
    
    //Admin: Delete Vehicle
    @DeleteMapping(value="/deleteVehicle/{vehicleType}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vehicleType) throws VentException
    {  
    	vehicleService.deleteVehicle(vehicleType);
    	String message="Vehicle "+vehicleType+" "+environment.getProperty("API.VEHICLE_DELETED_SUCCESSFULLY");
    	return new ResponseEntity<>(message,HttpStatus.OK);
    }
    
    //Get All Vehicles
    @GetMapping(value="/vehicles")
    public ResponseEntity<List<VehicleDTO>> getVehicles()
    {
    	List<VehicleDTO> vehicleList=vehicleService.getVehicles();
    	return new ResponseEntity<>(vehicleList,HttpStatus.OK);
    }
	
    //TO GET PARTICULAR VEHICLE Availability COUNT
    @GetMapping(value="/{vehicleType}")
    public ResponseEntity<Integer> getVehicleTypeCount(@PathVariable String vehicleType) throws VentException
    {
    	Integer vehicleTypeCount=vehicleService.getVehicleTypeCount(vehicleType);
    	return new ResponseEntity<>(vehicleTypeCount,HttpStatus.OK);
    }
    
    //To Decrement vehicleType Count upon it's booking
    @GetMapping(value="/{vehicleType}/{count}")
    public ResponseEntity<String> decVehicleTypeCount(@PathVariable String vehicleType,@PathVariable("count") Integer bookedCount) throws VentException
    {
    	vehicleService.decVehicleTypeCount(vehicleType, bookedCount);
    	return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
