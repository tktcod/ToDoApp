package com.sparta.todoapp.dto;


import com.sparta.todoapp.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleResponseDto {
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


    public ScheduleResponseDto(Schedule schedule){
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.createAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
