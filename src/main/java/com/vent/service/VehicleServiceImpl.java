package com.vent.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.vent.dto.VehicleDTO;
import com.vent.entity.Vehicles;
import com.vent.exception.VentException;
import com.vent.repository.VehicleRepository;

@Service(value="vehicleService")
public class VehicleServiceImpl implements VehicleService{

	@Autowired
	private VehicleRepository vehicleRepository;
	@Autowired
	private Environment environment;
	
	@Override
	public Integer addVehicle(VehicleDTO vehicleDTO) throws VentException{
		Vehicles vehicleTemp=vehicleRepository.findByVehicleType(vehicleDTO.getVehicleType());
		if(vehicleTemp!=null)
			throw new VentException(environment.getProperty("Service.VEHICLE_ALREADY_EXIST"));
		Vehicles vehicle=new Vehicles();
		vehicle.setVehicleType(vehicleDTO.getVehicleType());
		vehicle.setCount(vehicleDTO.getCount());
		vehicleRepository.save(vehicle);
		return vehicle.getCount();
	}

	
	@Override
	public Integer updateVehicle(VehicleDTO vehicleDTO) throws VentException{
		Vehicles vehicleTemp=vehicleRepository.findByVehicleType(vehicleDTO.getVehicleType());
		if(vehicleTemp==null)
			throw new VentException(environment.getProperty("Service.VEHICLE_DOESN'T_EXIST"));
		int newCount=vehicleTemp.getCount()+vehicleDTO.getCount();
		vehicleRepository.delete(vehicleTemp);
		vehicleTemp.setCount(newCount);
		vehicleRepository.save(vehicleTemp);
		
		return newCount;
	}
     
	
	@Override
	public void deleteVehicle(String vehicleType) throws VentException{
		Vehicles vehicleTemp=vehicleRepository.findByVehicleType(vehicleType);
		if(vehicleTemp==null)
			throw new VentException(environment.getProperty("Service.VEHICLE_DOESN'T_EXIST"));
		vehicleRepository.delete(vehicleTemp);

	}

	@Override
	public List<VehicleDTO> getVehicles() {
		Iterable<Vehicles> vehicleList=vehicleRepository.findAll();
		List<VehicleDTO> vehicleDTOs=new ArrayList<>();
		vehicleList.forEach(vehicle->{
			VehicleDTO vehicleDTO=new VehicleDTO();
			vehicleDTO.setVehicleType(vehicle.getVehicleType());
			vehicleDTO.setCount(vehicle.getCount());
			vehicleDTOs.add(vehicleDTO);
		});
		
		return vehicleDTOs;
	}


	@Override
	public Integer getVehicleTypeCount(String vehicleType) throws VentException{
		Vehicles vehicleTemp=vehicleRepository.findByVehicleType(vehicleType);
		if(vehicleTemp==null)
			throw new VentException(environment.getProperty("Service.VEHICLE_DOESN'T_EXIST"));
		return vehicleTemp.getCount();
	}


	@Override
	public void decVehicleTypeCount(String vehicleType, int bookedCount) throws VentException{
		Vehicles vehicleTemp=vehicleRepository.findByVehicleType(vehicleType);
		if(vehicleTemp==null)
			throw new VentException(environment.getProperty("Service.VEHICLE_DOESN'T_EXIST"));
		if(vehicleTemp.getCount()<bookedCount)
			throw new VentException(environment.getProperty("Service.BOOKED_BEYOND_CAPACITY"));
		Vehicles vehicle=new Vehicles();
		vehicle.setVehicleType(vehicleType);
	    vehicle.setCount(vehicleTemp.getCount()-bookedCount);
	    vehicleRepository.delete(vehicleTemp);
	    vehicleRepository.save(vehicle);
	}

	
	
}
