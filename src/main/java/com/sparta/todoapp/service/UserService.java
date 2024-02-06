package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<Map<String, String>> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        Map<String, String> responseBody = new HashMap<>();


        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            responseBody.put("message", "중복된 username 입니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        responseBody.put("message", "회원가입이 완료되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
