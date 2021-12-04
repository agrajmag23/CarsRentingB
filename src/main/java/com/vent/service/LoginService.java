package com.vent.service;

import com.vent.dto.UserDTO;
import com.vent.exception.VentException;

public interface LoginService {
	
	public UserDTO validate(String username) throws VentException;
	
	public Integer register(UserDTO userDTO) throws VentException;
}
