package com.hont.hont_backend.workout.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class RoutineRequest {

    @NotBlank(message = "루틴 이름을 입력해주세요")
    private String name;

    private String description;

    private List<RoutineExerciseRequest> exercises;

    @Getter
    public static class RoutineExerciseRequest {
        private Long exerciseId;
        private int targetSets;
        private int targetReps;
        private double targetWeight;
        private int orderIndex;
    }
}
