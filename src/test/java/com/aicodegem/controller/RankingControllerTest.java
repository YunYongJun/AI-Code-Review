package com.aicodegem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.aicodegem.model.Ranking;
import com.aicodegem.service.RankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RankingControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RankingService rankingService;

        private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        @Test // 특정 사용자의 순위조회
        public void testGetRanking() throws Exception {
                Long userId = 1L;
                Ranking ranking = new Ranking(userId, null, 1, 100, null);

                when(rankingService.getRankingByUserId(userId)).thenReturn(ranking);

                ResultActions resultActions = mockMvc.perform(get("/api/rankings/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions.andExpect(status().isOk())
                                .andExpect(jsonPath("$.userRank").value(ranking.getUserRank()))
                                .andExpect(jsonPath("$.totalScore").value(ranking.getTotalScore()));
        }

        @Test // 순위 조회 실패
        public void testGetRanking_NotFound() throws Exception {
                Long userId = 1L;

                when(rankingService.getRankingByUserId(userId)).thenReturn(null);

                ResultActions resultActions = mockMvc.perform(get("/api/rankings/{userId}", userId)
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions.andExpect(status().isNotFound());
        }

        @Test // 순위 저장
        public void testCreateRanking() throws Exception {
                // User 객체를 포함한 Ranking 객체 생성
                com.aicodegem.model.User user = new com.aicodegem.model.User();
                user.setId(1L); // 실제 ID 설정

                Ranking ranking = new Ranking(null, user, 1, 100, null);
                Ranking savedRanking = new Ranking(null, user, 1, 100, null);

                when(rankingService.saveRanking(any(Ranking.class))).thenReturn(savedRanking);

                ResultActions resultActions = mockMvc.perform(post("/api/rankings")
                                .content(objectMapper.writeValueAsString(ranking))
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions.andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(savedRanking.getId()))
                                .andExpect(jsonPath("$.userRank").value(savedRanking.getUserRank()))
                                .andExpect(jsonPath("$.totalScore").value(savedRanking.getTotalScore()));
        }

        @Test // 전체 사용자의 순위조회
        public void testGetAllRankings() throws Exception {
                Ranking ranking1 = new Ranking(null, null, 1, 100, null);
                Ranking ranking2 = new Ranking(null, null, 2, 90, null);

                when(rankingService.getAllRankings()).thenReturn(List.of(ranking1, ranking2));

                ResultActions resultActions = mockMvc.perform(get("/api/rankings")
                                .contentType(MediaType.APPLICATION_JSON));

                resultActions.andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].userRank").value(ranking1.getUserRank()))
                                .andExpect(jsonPath("$[1].userRank").value(ranking2.getUserRank()));
        }
}
