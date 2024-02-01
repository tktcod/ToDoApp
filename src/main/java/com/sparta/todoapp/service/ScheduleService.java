package com.sparta.todoapp.service;


import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.save(new Schedule(requestDto));
        return new ScheduleResponseDto(schedule);
    }
}
