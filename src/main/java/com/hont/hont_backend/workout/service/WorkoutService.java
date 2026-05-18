package com.hont.hont_backend.workout.service;

import com.hont.hont_backend.auth.entity.User;
import com.hont.hont_backend.auth.repository.UserRepository;
import com.hont.hont_backend.workout.dto.*;
import com.hont.hont_backend.workout.entity.*;
import com.hont.hont_backend.workout.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkoutService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;
    private final WorkoutLogRepository workoutLogRepository;

    // 운동 종목 전체 조회
    public List<Exercise> getExercises(String bodyPart) {
        if (bodyPart != null) {
            return exerciseRepository.findByBodyPart(bodyPart);
        }
        return exerciseRepository.findAll();
    }

    // 루틴 생성
    @Transactional
    public RoutineResponse createRoutine(Long userId, RoutineRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        Routine routine = Routine.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        if (request.getExercises() != null) {
            for (RoutineRequest.RoutineExerciseRequest req : request.getExercises()) {
                Exercise exercise = exerciseRepository.findById(req.getExerciseId())
                        .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다"));
                routine.getRoutineExercises().add(RoutineExercise.builder()
                        .routine(routine)
                        .exercise(exercise)
                        .targetSets(req.getTargetSets())
                        .targetReps(req.getTargetReps())
                        .targetWeight(req.getTargetWeight())
                        .orderIndex(req.getOrderIndex())
                        .build());
            }
        }

        return new RoutineResponse(routineRepository.save(routine));
    }

    // 내 루틴 목록 조회
    public List<RoutineResponse> getMyRoutines(Long userId) {
        return routineRepository.findByUserId(userId).stream()
                .map(RoutineResponse::new)
                .toList();
    }

    // 루틴 상세 조회
    public RoutineResponse getRoutine(Long userId, Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다"));
        if (!routine.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다");
        }
        return new RoutineResponse(routine);
    }

    // 루틴 수정
    @Transactional
    public RoutineResponse updateRoutine(Long userId, Long routineId, RoutineRequest request) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다"));
        if (!routine.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다");
        }
        routine.update(request.getName(), request.getDescription());
        return new RoutineResponse(routine);
    }

    // 루틴 삭제
    @Transactional
    public void deleteRoutine(Long userId, Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다"));
        if (!routine.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다");
        }
        routineRepository.delete(routine);
    }

    // 운동 기록 저장
    @Transactional
    public WorkoutLogResponse saveWorkoutLog(Long userId, WorkoutLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        Routine routine = null;
        if (request.getRoutineId() != null) {
            routine = routineRepository.findById(request.getRoutineId()).orElse(null);
        }

        WorkoutLog log = WorkoutLog.builder()
                .user(user)
                .routine(routine)
                .workoutDate(request.getWorkoutDate())
                .durationMinutes(request.getDurationMinutes())
                .memo(request.getMemo())
                .build();

        if (request.getSets() != null) {
            for (WorkoutLogRequest.WorkoutSetRequest setReq : request.getSets()) {
                Exercise exercise = exerciseRepository.findById(setReq.getExerciseId())
                        .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다"));
                log.getWorkoutSets().add(WorkoutSet.builder()
                        .workoutLog(log)
                        .exercise(exercise)
                        .setNumber(setReq.getSetNumber())
                        .weight(setReq.getWeight())
                        .reps(setReq.getReps())
                        .completed(setReq.isCompleted())
                        .build());
            }
        }

        return new WorkoutLogResponse(workoutLogRepository.save(log));
    }

    // 특정 날짜 운동 기록 조회
    public WorkoutLogResponse getWorkoutLog(Long userId, LocalDate date) {
        return workoutLogRepository.findByUserIdAndWorkoutDate(userId, date)
                .map(WorkoutLogResponse::new)
                .orElse(null);
    }

    // 월별 운동 기록 조회 (캘린더용)
    public List<WorkoutLogResponse> getMonthlyWorkoutLogs(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return workoutLogRepository.findByUserIdAndWorkoutDateBetween(userId, start, end).stream()
                .map(WorkoutLogResponse::new)
                .toList();
    }
}
