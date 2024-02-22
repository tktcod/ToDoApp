package com.sparta.todoapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
