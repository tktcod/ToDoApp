package com.sparta.todoapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotNull
    @Pattern(regexp="^[a-z0-9]{4,10}$")
    private String username;

    @NotNull
    @Pattern(regexp="^[a-zA-Z0-9]{8,15}$")
    private String password;
}
