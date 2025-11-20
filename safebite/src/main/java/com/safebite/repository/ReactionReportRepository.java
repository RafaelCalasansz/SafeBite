package com.safebite.repository;

import com.safebite.model.ReactionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionReportRepository extends JpaRepository<ReactionReport, Long> {
    List<ReactionReport> findByIntensidadeSintomas(String intensidade);
}