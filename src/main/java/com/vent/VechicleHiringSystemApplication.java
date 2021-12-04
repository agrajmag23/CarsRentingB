package com.vent;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.vent.controller.LoginController;

@SpringBootApplication
public class VechicleHiringSystemApplication implements CommandLineRunner{
	
//	@Autowired
//	private LoginController loginController;

	public static void main(String[] args) {
		SpringApplication.run(VechicleHiringSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		loginController.validateCred();
	}

}
