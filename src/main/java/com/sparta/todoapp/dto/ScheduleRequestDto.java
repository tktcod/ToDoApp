package com.sparta.todoapp.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String contents;
}
