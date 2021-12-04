package com.vent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vent.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment,Integer>{

}
