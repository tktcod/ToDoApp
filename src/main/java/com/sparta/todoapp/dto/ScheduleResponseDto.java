package com.sparta.todoapp.dto;


import com.sparta.todoapp.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String contents;
    private Boolean isFinished;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.isFinished = schedule.getIsFinished();
        this.createAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
