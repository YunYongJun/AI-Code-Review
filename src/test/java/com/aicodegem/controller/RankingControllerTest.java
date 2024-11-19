package com.aicodegem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.aicodegem.model.Ranking;
import com.aicodegem.security.JwtUtil;
import com.aicodegem.service.RankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
public class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingService rankingService;

    @MockBean
    private JwtUtil jwtUtil;

    private String jwtToken;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setUp() {
        // JWT 토큰을 생성하기 위해 가짜 UserDetails 생성
        UserDetails mockUserDetails = User.withUsername("usertest")
                .password("password")
                .roles("USER")
                .build();

        // JWT 토큰 생성
        jwtToken = jwtUtil.generateToken(mockUserDetails, "USER", "usertest");

        // JwtUtil이 UserDetails를 반환하도록 설정
        when(jwtUtil.validateToken(any(String.class), any(UserDetails.class))).thenReturn(true);
        when(jwtUtil.extractUsername(any(String.class))).thenReturn("usertest");
    }

    @Test // 특정 사용자의 순위조회
    @WithMockUser(roles = "USER")
    public void testGetRanking() throws Exception {
        Long userId = 1L;
        Ranking ranking = new Ranking(1, null, 1, 100, null);

        when(rankingService.getRankingByUserId(userId)).thenReturn(ranking);

        ResultActions resultActions = mockMvc.perform(get("/api/rankings/{userId}", userId)
                .header("Authorization", "Bearer " + jwtToken) // JWT 토큰을 헤더에 추가
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.userRank").value(ranking.getUserRank()))
                .andExpect(jsonPath("$.totalScore").value(ranking.getTotalScore()));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetRanking_NotFound() throws Exception {
        Long userId = 1L;

        when(rankingService.getRankingByUserId(userId)).thenReturn(null);

        ResultActions resultActions = mockMvc.perform(get("/api/rankings/{userId}", userId)
                .header("Authorization", "Bearer " + jwtToken) // JWT 토큰을 헤더에 추가
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateRanking() throws Exception {

        // UserDetails를 com.aicodegem.model.User로 변환
        UserDetails springSecurityUser = User.builder().username("testUser").password("testPassword").roles("USER")
                .build();
        com.aicodegem.model.User user = new com.aicodegem.model.User();
        user.setId(1L); // 실제 ID 설정

        // User 객체를 포함한 Ranking 객체 생성
        Ranking ranking = new Ranking(null, user, 1, 100, null);
        Ranking savedRanking = new Ranking(1, user, 1, 100, null);

        when(rankingService.saveRanking(any(Ranking.class))).thenReturn(savedRanking);

        ResultActions resultActions = mockMvc.perform(post("/api/rankings")
                .header("Authorization", "Bearer " + jwtToken) // JWT 토큰을 헤더에 추가
                .content(objectMapper.writeValueAsString(ranking))
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRanking.getId()))
                .andExpect(jsonPath("$.userRank").value(savedRanking.getUserRank()))
                .andExpect(jsonPath("$.totalScore").value(savedRanking.getTotalScore()));
    }

    @Test // 전체 사용자의 순위조회
    @WithMockUser(roles = "USER")
    public void testGetAllRankings() throws Exception {
        Ranking ranking1 = new Ranking(1, null, 1, 100, null);
        Ranking ranking2 = new Ranking(2, null, 2, 90, null);

        when(rankingService.getAllRankings()).thenReturn(List.of(ranking1, ranking2));

        ResultActions resultActions = mockMvc.perform(get("/api/rankings")
                .header("Authorization", "Bearer " + jwtToken) // JWT 토큰을 헤더에 추가
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userRank").value(ranking1.getUserRank()))
                .andExpect(jsonPath("$[1].userRank").value(ranking2.getUserRank()));
    }
}
