package com.hont.hont_backend.workout.dto;

import com.hont.hont_backend.workout.entity.WorkoutLog;
import com.hont.hont_backend.workout.entity.WorkoutSet;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class WorkoutLogResponse {

    private final Long id;
    private final LocalDate workoutDate;
    private final String routineName;
    private final int durationMinutes;
    private final String memo;
    private final List<WorkoutSetResponse> sets;

    public WorkoutLogResponse(WorkoutLog log) {
        this.id = log.getId();
        this.workoutDate = log.getWorkoutDate();
        this.routineName = log.getRoutine() != null ? log.getRoutine().getName() : null;
        this.durationMinutes = log.getDurationMinutes();
        this.memo = log.getMemo();
        this.sets = log.getWorkoutSets().stream()
                .map(WorkoutSetResponse::new)
                .toList();
    }

    @Getter
    public static class WorkoutSetResponse {
        private final Long id;
        private final String exerciseName;
        private final int setNumber;
        private final double weight;
        private final int reps;
        private final boolean completed;

        public WorkoutSetResponse(WorkoutSet set) {
            this.id = set.getId();
            this.exerciseName = set.getExercise().getName();
            this.setNumber = set.getSetNumber();
            this.weight = set.getWeight();
            this.reps = set.getReps();
            this.completed = set.isCompleted();
        }
    }
}
