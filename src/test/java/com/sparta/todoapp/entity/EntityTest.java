package com.sparta.todoapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityTest {

    @Nested
    @DisplayName("User Entity Test")
    class UserTest {
        @Test
        void test1() {
            // given
            String username = "testusername";
            String password = "testpassword";

            // when
            User user = new User(username, password);

            //then
            assertEquals(username, user.getUsername());
        }

    }
}
