package com.aicodegem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.aicodegem.model.Achievement;
import com.aicodegem.model.UserAchievement;
import com.aicodegem.security.JwtRequestFilter;
import com.aicodegem.security.JwtUtil;
import com.aicodegem.service.AchievementService;
import com.aicodegem.service.UserAchievementService;

@SpringBootTest
@AutoConfigureMockMvc
public class AchievementControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AchievementService achievementService;

        @MockBean
        private UserAchievementService userAchievementService;

        @MockBean
        private JwtRequestFilter jwtRequestFilter; // JwtRequestFilter Mock 추가

        @MockBean
        private JwtUtil jwtUtil; // JwtUtil Mock 추가

        private Achievement achievement;
        private UserAchievement userAchievement;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);

                // Test data setup
                achievement = new Achievement(1L, "Achievement 1", "First Achievement", "total_score >= 100");
                userAchievement = new UserAchievement(1L, null, achievement, null);
        }

        @Test
        @WithMockUser(username = "testuser", roles = { "USER" })
        public void testGetAllAchievements() throws Exception {
                List<Achievement> achievements = Arrays.asList(achievement);

                // Mock service call
                when(achievementService.getAllAchievements()).thenReturn(achievements);

                // Mock JwtUtil behavior for extracting username (security-related)
                when(jwtUtil.extractUsername(anyString())).thenReturn("testuser");

                // Perform GET request to /api/achievements
                mockMvc.perform(get("/api/achievements")
                                .header("Authorization", "Bearer testToken")) // JWT 토큰을 Authorization 헤더에 추가
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value("1")) // Adjusted path
                                .andExpect(jsonPath("$[0].achievementName").value("Achievement 1")) // Adjusted path
                                .andExpect(jsonPath("$[0].achievementDesc").value("First Achievement"))
                                .andExpect(jsonPath("$[0].criteria").value("total_score >= 10")); // Adjusted path

                // Verify if service method is called
                verify(achievementService, times(1)).getAllAchievements();
        }

        // **2. 특정 사용자의 업적 조회 (UserAchievement 테이블)**
        @Test
        public void testGetUserAchievements() throws Exception {
                List<UserAchievement> userAchievements = Arrays.asList(userAchievement);

                // Mock service call
                when(userAchievementService.getAchievementsByUserId(1L)).thenReturn(userAchievements);

                // Mock JwtUtil behavior for extracting username (security-related)
                when(jwtUtil.extractUsername(anyString())).thenReturn("testuser");

                // Perform GET request to /api/achievements/{userId}
                mockMvc.perform(get("/api/achievements/1")
                                .header("Authorization", "Bearer testToken")) // JWT 토큰을 Authorization 헤더에 추가
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].achievement.achievementName").value("Achievement 1"));

                // Verify if service method is called
                verify(userAchievementService, times(1)).getAchievementsByUserId(1L);
        }

        // **3. 새 업적 저장 (Achievement 테이블)**
        @Test
        public void testCreateAchievement() throws Exception {
                // Mock service call
                when(achievementService.saveAchievement(any(Achievement.class))).thenReturn(achievement);

                // Mock JwtUtil behavior for extracting username (security-related)
                when(jwtUtil.extractUsername(anyString())).thenReturn("testuser");

                // Perform POST request to /api/achievements/achievement
                mockMvc.perform(post("/api/achievements/achievement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"achievementName\": \"Achievement 1\", \"achievementDesc\": \"First Achievement\", \"criteria\": \"total_score >= 100\"}")
                                .header("Authorization", "Bearer testToken")) // JWT 토큰을 Authorization 헤더에 추가
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.achievementName").value("Achievement 1"))
                                .andExpect(jsonPath("$.achievementDesc").value("First Achievement"));

                // Verify if service method is called
                verify(achievementService, times(1)).saveAchievement(any(Achievement.class));
        }
}
