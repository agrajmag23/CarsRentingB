package com.vent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.vent.exception.VentException;
import com.vent.service.LoginService;

@Controller(value="LoginController")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	public void validateCred() throws VentException{
		
		System.out.println(loginService.validate("admin"));
		
		
	}
}
