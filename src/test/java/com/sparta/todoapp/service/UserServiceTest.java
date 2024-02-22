package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    public void signupSuccess() {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("newUser");
        requestDto.setPassword("password");

        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("hashedPassword");

        // When
        ResponseEntity<Map<String, String>> responseEntity = userService.signup(requestDto);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("회원가입이 완료되었습니다.", responseEntity.getBody().get("message"));
    }

    @Test
    @DisplayName("회원가입 중복으로 인한 실패")
    public void signupFailure() {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("existingUser");
        requestDto.setPassword("password");

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));

        // When
        ResponseEntity<Map<String, String>> responseEntity = userService.signup(requestDto);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("중복된 username 입니다.", responseEntity.getBody().get("message"));
    }
}