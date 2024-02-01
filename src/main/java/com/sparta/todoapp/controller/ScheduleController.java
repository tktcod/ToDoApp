package com.sparta.todoapp.controller;


import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto){
        return  scheduleService.createSchedule(requestDto);
    }
}
