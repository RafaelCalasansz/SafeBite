package com.safebite.repository;

import com.safebite.model.EmergencyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCardRepository extends JpaRepository<EmergencyCard, Long> {

}