package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserByUsername() {
        // Given
        String username = "testUser";
        String password = "password";
        User user = new User(username, password);
        userRepository.save(user);

        // When
        User foundUser = userRepository.findByUsername(username).orElse(null);

        // Then
        assertEquals(username, foundUser.getUsername());
    }
}