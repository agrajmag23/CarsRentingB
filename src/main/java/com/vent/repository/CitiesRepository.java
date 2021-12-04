package com.vent.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vent.entity.Cities;

@Repository("citiesRepository")
public interface CitiesRepository extends CrudRepository<Cities,Integer>{

	@Query("select longitude from Cities c where c.city=?1")
	public String findLongitudeByCity(String city);
	@Query("select latitude from Cities c where c.city=?1")
	public String findLatitudeByCity(String city);
	@Query("select city,state from Cities c")
	public List<List<String>> findAllCity();
}
