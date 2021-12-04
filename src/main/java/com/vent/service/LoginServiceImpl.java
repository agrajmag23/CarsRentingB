package com.vent.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.vent.dto.UserDTO;
import com.vent.entity.User;
import com.vent.exception.VentException;
import com.vent.repository.LoginRepository;
import com.vent.service.LoginService;

@Service(value="LoginService")
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private Environment environment;
	
	
	@Override
	public UserDTO validate(String  username) throws VentException{
		
		Optional<User> credOpt = loginRepository.findByuserName(username);
		
		if(credOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.USER_DOESN'T_EXIST"));
		User user = credOpt.get();
		
		UserDTO actualCred = new UserDTO();
		actualCred.setId(user.getId());
		actualCred.setUserName(user.getUserName());
		actualCred.setPassword(user.getPassword());
		actualCred.setRole(user.getRole());
		
		
		return actualCred;
	}

	@Override
	public Integer register(UserDTO userDTO) throws VentException{
		
		Optional<User> credOpt=loginRepository.findByuserName(userDTO.getUserName());
		if(!credOpt.isEmpty())
			throw new VentException(environment.getProperty("Service.USER_ALREADY_EXIST"));
		User user = new User();
		user.setId(userDTO.getId());
		user.setUserName(userDTO.getUserName());
		user.setPassword(userDTO.getPassword());
		user.setRole(userDTO.getRole());
		
		return loginRepository.save(user).getId();
	}
	
	

}
