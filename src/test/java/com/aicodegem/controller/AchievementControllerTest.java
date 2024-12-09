package com.aicodegem.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.aicodegem.model.Achievement;
import com.aicodegem.model.UserAchievement;
import com.aicodegem.service.AchievementService;
import com.aicodegem.service.UserAchievementService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AchievementControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AchievementService achievementService;

        @MockBean
        private UserAchievementService userAchievementService;

        // 1. 모든 업적 조회 테스트
        @Test
        public void testGetAllAchievements() throws Exception {
                Achievement achievement1 = new Achievement(1L, "Achievement 1", "Description 1", "total_score >= 100");
                Achievement achievement2 = new Achievement(2L, "Achievement 2", "Description 2", "total_score >= 200");

                List<Achievement> achievements = Arrays.asList(achievement1, achievement2);

                Mockito.when(achievementService.getAllAchievements()).thenReturn(achievements);

                mockMvc.perform(get("/api/achievements")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].achievementName", is("Achievement 1")))
                                .andExpect(jsonPath("$[1].achievementName", is("Achievement 2")));
        }

        // 2. 사용자별 업적 조회 테스트
        @Test
        public void testGetUserAchievements() throws Exception {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setId(1L);

                Mockito.when(userAchievementService.getAchievementsByUserId(eq(1L)))
                                .thenReturn(Collections.singletonList(userAchievement));

                mockMvc.perform(get("/api/achievements/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id", is(1)));
        }

        // 3. 새 업적 저장 테스트
        @Test
        public void testCreateAchievement() throws Exception {
                Achievement newAchievement = new Achievement(null, "New Achievement", "New Description",
                                "total_score >= 150");
                Achievement savedAchievement = new Achievement(1L, "New Achievement", "New Description",
                                "total_score >= 150");

                Mockito.when(achievementService.saveAchievement(any(Achievement.class))).thenReturn(savedAchievement);

                mockMvc.perform(post("/api/achievements/achievement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "achievementName": "New Achievement",
                                                    "achievementDesc": "New Description",
                                                    "criteria": "total_score >= 150"
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.achievementName", is("New Achievement")));
        }

        // 4. 업적 조회 실패 테스트 (업적 없음)
        @Test
        public void testGetAllAchievements_NoAchievements() throws Exception {
                Mockito.when(achievementService.getAllAchievements()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/api/achievements")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));
        }
}
