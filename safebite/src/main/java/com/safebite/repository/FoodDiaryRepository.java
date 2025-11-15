package com.safebite.repository;

import com.safebite.model.FoodDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    List<FoodDiary> findByUserIdOrderByDataHoraDesc(Long userId);
}