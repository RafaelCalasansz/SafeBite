package com.safebite.repository;

import com.safebite.model.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
    Optional<Restriction> findByNome(String nome);
}