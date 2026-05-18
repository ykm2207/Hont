package com.hont.hont_backend.workout.repository;

import com.hont.hont_backend.workout.entity.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    // 특정 날짜 운동 기록
    Optional<WorkoutLog> findByUserIdAndWorkoutDate(Long userId, LocalDate workoutDate);

    // 월별 운동 기록 (캘린더용)
    List<WorkoutLog> findByUserIdAndWorkoutDateBetween(Long userId, LocalDate start, LocalDate end);
}
