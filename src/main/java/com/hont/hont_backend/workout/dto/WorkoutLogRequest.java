package com.hont.hont_backend.workout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class WorkoutLogRequest {

    @NotNull(message = "운동 날짜를 입력해주세요")
    private LocalDate workoutDate;

    private Long routineId;

    private int durationMinutes;

    private String memo;

    private List<WorkoutSetRequest> sets;

    @Getter
    public static class WorkoutSetRequest {
        private Long exerciseId;
        private int setNumber;
        private double weight;
        private int reps;
        private boolean completed;
    }
}
