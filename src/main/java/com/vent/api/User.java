package com.vent.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vent.dto.UserDTO;
import com.vent.exception.VentException;
import com.vent.service.LoginService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class User {
	
	//API.USER_ADDED_SUCCESSFULLY
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private Environment environment;
	

	@GetMapping("/login/{username}")
	public ResponseEntity<UserDTO> login(@PathVariable String username) throws VentException{
		UserDTO user = loginService.validate(username);
		return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
	}
	

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDTO userDTO) throws VentException{
		Integer id = loginService.register(userDTO);
		String message=userDTO.getUserName()+" "+environment.getProperty("API.USER_ADDED_SUCCESSFULLY")+" "+id;
		//String msg = "User Successfully Registered with ID: " + id + ", Name: " + userDTO.getUserName();
		return new ResponseEntity<>(message,HttpStatus.ACCEPTED); 
	}
}
