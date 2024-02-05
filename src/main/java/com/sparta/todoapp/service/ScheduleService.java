package com.sparta.todoapp.service;


import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 할 일을 찾을 수 없습니다.")
        );

        // 작성자 확인
        if(!schedule.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("해당 할 일 작성자가 아닙니다.");
        }

        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateTaskCompletionSchedule(Long id, Boolean isFinished, User user) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 할 일을 찾을 수 없습니다.")
        );

        // 작성자 확인
        if(!schedule.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("해당 할 일 작성자가 아닙니다.");
        }

        schedule.update(isFinished);
        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getAllSchedule() {
        List<Schedule> scheduleList = scheduleRepository.findAllByOrderByCreatedAtDesc();
        List<ScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            responseDtoList.add(new ScheduleResponseDto(schedule));
        }

        return responseDtoList;
    }

    public ScheduleResponseDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 할 일이 존재하지 않습니다.")
        );
        return new ScheduleResponseDto(schedule);
    }

    public Long deleteSchedule(Long id, User user) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 할 일을 찾을 수 없습니다.")
        );

        // 작성자 확인
        if(!schedule.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("해당 할 일 작성자가 아닙니다.");
        } else {
            scheduleRepository.delete(schedule);
        }

        return id;
    }
}
