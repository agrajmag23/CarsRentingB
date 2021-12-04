package com.vent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vent.entity.User;

@Repository(value="LoginRepository")
public interface LoginRepository extends JpaRepository<User, Integer>{
	
	public Optional<User> findByuserName(String userName);
	
//	public User save(User user);
	@Override
	public <S extends User> S save(S user);
	
}
