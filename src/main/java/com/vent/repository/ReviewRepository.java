package com.vent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vent.entity.Review;

public interface ReviewRepository extends JpaRepository<Review,Integer>{

}
