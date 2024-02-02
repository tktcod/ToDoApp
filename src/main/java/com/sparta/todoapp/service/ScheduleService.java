package com.sparta.todoapp.service;


import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user) {
        Schedule schedule = scheduleRepository.save(new Schedule(requestDto, user));
        return new ScheduleResponseDto(schedule);
    }

    public ScheduleResponseDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 할 일이 존재하지 않습니다.")
        );
        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getAllSchedule() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        List<ScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            responseDtoList.add(new ScheduleResponseDto(schedule));
        }

        return responseDtoList;
    }
}
