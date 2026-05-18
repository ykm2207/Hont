package com.hont.hont_backend.diet.repository;

import com.hont.hont_backend.diet.entity.DietLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DietLogRepository extends JpaRepository<DietLog, Long> {

    Optional<DietLog> findByUserIdAndDietDate(Long userId, LocalDate dietDate);

    List<DietLog> findByUserIdAndDietDateBetween(Long userId, LocalDate start, LocalDate end);
}
