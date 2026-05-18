package com.hont.hont_backend.workout.controller;

import com.hont.hont_backend.common.response.ApiResponse;
import com.hont.hont_backend.workout.dto.*;
import com.hont.hont_backend.workout.entity.Exercise;
import com.hont.hont_backend.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    // 운동 종목 목록 조회
    @GetMapping("/exercises")
    public ApiResponse<List<Exercise>> getExercises(@RequestParam(required = false) String bodyPart) {
        return ApiResponse.ok(workoutService.getExercises(bodyPart));
    }

    // 루틴 생성
    @PostMapping("/routines")
    public ApiResponse<RoutineResponse> createRoutine(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RoutineRequest request) {
        return ApiResponse.ok(workoutService.createRoutine(userId, request));
    }

    // 내 루틴 목록 조회
    @GetMapping("/routines")
    public ApiResponse<List<RoutineResponse>> getMyRoutines(@AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(workoutService.getMyRoutines(userId));
    }

    // 루틴 상세 조회
    @GetMapping("/routines/{routineId}")
    public ApiResponse<RoutineResponse> getRoutine(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long routineId) {
        return ApiResponse.ok(workoutService.getRoutine(userId, routineId));
    }

    // 루틴 수정
    @PutMapping("/routines/{routineId}")
    public ApiResponse<RoutineResponse> updateRoutine(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long routineId,
            @Valid @RequestBody RoutineRequest request) {
        return ApiResponse.ok(workoutService.updateRoutine(userId, routineId, request));
    }

    // 루틴 삭제
    @DeleteMapping("/routines/{routineId}")
    public ApiResponse<Void> deleteRoutine(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long routineId) {
        workoutService.deleteRoutine(userId, routineId);
        return ApiResponse.ok("루틴이 삭제되었습니다", null);
    }

    // 운동 기록 저장
    @PostMapping("/logs")
    public ApiResponse<WorkoutLogResponse> saveWorkoutLog(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody WorkoutLogRequest request) {
        return ApiResponse.ok(workoutService.saveWorkoutLog(userId, request));
    }

    // 특정 날짜 운동 기록 조회
    @GetMapping("/logs/{date}")
    public ApiResponse<WorkoutLogResponse> getWorkoutLog(
            @AuthenticationPrincipal Long userId,
            @PathVariable LocalDate date) {
        return ApiResponse.ok(workoutService.getWorkoutLog(userId, date));
    }

    // 월별 운동 기록 조회
    @GetMapping("/logs")
    public ApiResponse<List<WorkoutLogResponse>> getMonthlyWorkoutLogs(
            @AuthenticationPrincipal Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return ApiResponse.ok(workoutService.getMonthlyWorkoutLogs(userId, year, month));
    }
}
