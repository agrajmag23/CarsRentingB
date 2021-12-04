package com.vent.service;

import java.util.List;

import com.vent.dto.VehicleDTO;
import com.vent.exception.VentException;

public interface VehicleService {

	public Integer addVehicle(VehicleDTO vehicleDTO) throws VentException;
	public Integer updateVehicle(VehicleDTO vehicleDTO) throws VentException;
	public void deleteVehicle(String vehicleType) throws VentException;
	public List<VehicleDTO> getVehicles();
	public Integer getVehicleTypeCount(String vehicleType) throws VentException;
	public void decVehicleTypeCount(String vehicleType,int bookedCount) throws VentException;
}
