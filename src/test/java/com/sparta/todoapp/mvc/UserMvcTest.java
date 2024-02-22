package com.sparta.todoapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.controller.UserController;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.security.WebSecurityConfig;
import com.sparta.todoapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
public class UserMvcTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @Test
    @DisplayName("로그인 Page")
    void test1() throws Exception {
        // when - then
        mockMvc.perform(get("/api/user/login-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void testSignup() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setPassword("password");

        // When - Then
        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 실패 - 필수 필드 누락")
    void testSignupFailureMissingFields() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();

        // When - 회원가입 요청을 보냄
        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("회원가입에 실패했습니다."));

    }

}
