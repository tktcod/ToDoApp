package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class ScheduleServiceIntegrationTest {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    public void createSchedule() {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("Test Title", "Test Contents");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        userRepository.save(user);

        // When
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, user);

        // Then
        assertEquals("Test Title", responseDto.getTitle());
        assertEquals("Test Contents", responseDto.getContents());

        Schedule savedSchedule = scheduleRepository.findById(responseDto.getId()).orElse(null);
        assertNotNull(savedSchedule);
        assertEquals("Test Title", savedSchedule.getTitle());
        assertEquals("Test Contents", savedSchedule.getContents());
    }
}