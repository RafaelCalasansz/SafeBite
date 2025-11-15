package com.safebite.repository;

import com.safebite.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByUserIdOrderByDataHoraReacaoDesc(Long userId);
}