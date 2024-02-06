package com.sparta.todoapp.controller;


import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.createSchedule(requestDto, userDetails.getUser());
    }

    @PutMapping("/schedule/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.updateSchedule(id, requestDto, userDetails.getUser());
    }

    @PutMapping("/schedule/{id}/{isFinished}")
    public ScheduleResponseDto updateTaskCompletionSchedule(@PathVariable Long id, @PathVariable Boolean isFinished, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.updateTaskCompletionSchedule(id, isFinished, userDetails.getUser());
    }

    @GetMapping("/schedule")
    public List<ScheduleResponseDto> getAllSchedule(){
        return scheduleService.getAllSchedule();
    }

    @GetMapping("/schedule/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long id){
        return scheduleService.getSchedule(id);
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.deleteSchedule(id, userDetails.getUser());
    }

}
