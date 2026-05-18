package com.hont.hont_backend.workout.repository;

import com.hont.hont_backend.workout.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByBodyPart(String bodyPart);
}
