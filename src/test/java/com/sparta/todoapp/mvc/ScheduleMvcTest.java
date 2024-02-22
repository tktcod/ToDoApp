package com.sparta.todoapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.controller.ScheduleController;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.security.WebSecurityConfig;
import com.sparta.todoapp.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {ScheduleController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
public class ScheduleMvcTest {

    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // mock 테스트 유저 생성
        String username = "testuser";
        String password = "password";
        User testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "");
    }

    @Nested
    @DisplayName("createSchedule Method")
    class createSchedule {

        @BeforeEach
        void setUp() {
            mockUserSetup();
        }
        @Test
        @DisplayName("Create Schedule Success")
        void createScheduleSuccess() throws Exception {
            // Given
            String title = "test title";
            String contents = "test contents";
            ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

            String postInfo = objectMapper.writeValueAsString(requestDto);

            // When - Then
            mockMvc.perform(post("/api/schedule")
                            .content(postInfo)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .principal(mockPrincipal)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("Create Schedule Failure 1 - No User Principle")
        void createScheduleFailure1() throws Exception {
            // Given
            String title = "test title";
            String contents = "test contents";
            ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

            String postInfo = objectMapper.writeValueAsString(requestDto);

            // When - Then
            mockMvc.perform(post("/api/schedule")
                            .content(postInfo)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Create Schedule Failure 2 - Missing Request Body")
        void createScheduleFailure2() throws Exception {
            // When - Then
            mockMvc.perform(post("/api/schedule")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .principal(mockPrincipal)
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("updateSchedule Method")
    class updateSchedule {

        @Test
        @DisplayName("Update Schedule Success")
        void updateScheduleSuccess() throws Exception{
            // Given
            Long scheduleId = 1L;
            String title = "Updated Title";
            String contents = "Updated Contents";
            ScheduleRequestDto requestDto = new ScheduleRequestDto(title, contents);

            UserDetailsImpl userDetails = new UserDetailsImpl(new User("testUser", "password"));

            given(scheduleService.updateSchedule(anyLong(), any(ScheduleRequestDto.class), any(User.class)))
                    .willReturn(new ScheduleResponseDto(new Schedule(requestDto, userDetails.getUser())));

            // When -Then
            mockMvc.perform(put("/api/schedule/{id}", scheduleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(new UsernamePasswordAuthenticationToken(userDetails, "")))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.title").value(title))
                    .andExpect(jsonPath("$.contents").value(contents));

        }
    }
}
