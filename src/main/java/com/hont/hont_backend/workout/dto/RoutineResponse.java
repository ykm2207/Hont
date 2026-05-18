package com.hont.hont_backend.workout.dto;

import com.hont.hont_backend.workout.entity.Routine;
import com.hont.hont_backend.workout.entity.RoutineExercise;
import lombok.Getter;

import java.util.List;

@Getter
public class RoutineResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final List<RoutineExerciseResponse> exercises;

    public RoutineResponse(Routine routine) {
        this.id = routine.getId();
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.exercises = routine.getRoutineExercises().stream()
                .map(RoutineExerciseResponse::new)
                .toList();
    }

    @Getter
    public static class RoutineExerciseResponse {
        private final Long id;
        private final Long exerciseId;
        private final String exerciseName;
        private final String bodyPart;
        private final int targetSets;
        private final int targetReps;
        private final double targetWeight;
        private final int orderIndex;

        public RoutineExerciseResponse(RoutineExercise re) {
            this.id = re.getId();
            this.exerciseId = re.getExercise().getId();
            this.exerciseName = re.getExercise().getName();
            this.bodyPart = re.getExercise().getBodyPart();
            this.targetSets = re.getTargetSets();
            this.targetReps = re.getTargetReps();
            this.targetWeight = re.getTargetWeight();
            this.orderIndex = re.getOrderIndex();
        }
    }
}
