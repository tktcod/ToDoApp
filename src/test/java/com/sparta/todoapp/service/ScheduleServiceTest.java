package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("createSchedule method")
    public void createSchedule() {
        // Given
        User user = new User();

        String title = "testTitle";
        String contents = "testContent";
        ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

        Schedule schedule = new Schedule(requestDto, user);

        given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);

        // When
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, user);

        // Then
        assertEquals(title, responseDto.getTitle());
        assertEquals(contents, responseDto.getContents());
    }

    @Nested
    @DisplayName("updateSchedule method")
    class updateSchedule {
        @Test
        @DisplayName("updateSchedule success")
        public void updateScheduleSuccess() {
            // Given
            User user = new User();
            user.setId(1L);

            Long scheduleId = 1L;
            String title = "Updated Title";
            String contents = "Updated Contents";
            ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

            Schedule schedule = new Schedule();
            schedule.setUser(user);
            schedule.setTitle("title");
            schedule.setContents("contents");

            given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));

            // When
            ScheduleResponseDto responseDto = scheduleService.updateSchedule(scheduleId, requestDto, user);

            // Then
            assertEquals(title, responseDto.getTitle());
            assertEquals(contents, responseDto.getContents());
        }

        @Test
        @DisplayName("updateSchedule failure 1 - 해당 할일 없음")
        public void updateScheduleFailure1() {
            // Given
            User user = new User();
            user.setId(1L);

            Long scheduleId = 1L;
            String title = "Updated Title";
            String contents = "Updated Contents";
            ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

            Schedule schedule = new Schedule();
            schedule.setUser(user);

            given(scheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

            // When
            Exception exception = assertThrows(NullPointerException.class,
                    () -> scheduleService.updateSchedule(scheduleId, requestDto, user)
            );

            // Then
            assertEquals("해당 할 일을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("updateSchedule failure 2 - 해당 할일 작성자가 아님")
        public void updateScheduleFailure2() {
            // Given
            User currentUser = new User();
            currentUser.setId(1L);
            User otherUser = new User();
            otherUser.setId(2L);

            Long scheduleId = 1L;
            Schedule schedule = new Schedule();
            schedule.setUser(otherUser); // 다른 사용자가 작성한 할일

            String title = "Updated Title";
            String contents = "Updated Contents";
            ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

            given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));

            // When
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> scheduleService.updateSchedule(scheduleId, requestDto, currentUser)
            );

            // Then
            assertEquals("해당 할 일 작성자가 아닙니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getSchedule method")
    class getSchedule {
        @Test
        @DisplayName("getSchedule success")
        public void getScheduleSuccess() {
            // Given
            Long scheduleId = 1L;
            String title = "Schedule title";
            String contents = "Schedule contents";
            Schedule schedule = new Schedule();
            schedule.setId(scheduleId);
            schedule.setTitle(title);
            schedule.setContents(contents);

            given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));

            // When
            ScheduleResponseDto responseDto = scheduleService.getSchedule(scheduleId);

            // Then
            assertEquals(title, responseDto.getTitle());
            assertEquals(contents, responseDto.getContents());
        }

        @Test
        @DisplayName("getSchedule failure")
        public void getScheduleFailure() {
            // Given
            Long scheduleId = 1L;

            given(scheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

            // When
            Exception exception = assertThrows(NullPointerException.class,
                    () -> scheduleService.getSchedule(scheduleId)
            );

            // Then
            assertEquals("해당 할 일이 존재하지 않습니다.", exception.getMessage());
        }
    }
}