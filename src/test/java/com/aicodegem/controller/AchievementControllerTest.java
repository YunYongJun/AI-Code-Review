// package com.aicodegem.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.time.LocalDate;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;

// import com.aicodegem.model.Achievement;
// import com.aicodegem.model.UserAchievement;
// import com.aicodegem.security.JwtUtil;
// import com.aicodegem.service.AchievementService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class AchievementControllerTest {

// @Autowired
// private MockMvc mockMvc;

// @MockBean
// private AchievementService achievementService;

// @MockBean
// private JwtUtil jwtUtil;

// private String jwtToken;

// private final ObjectMapper objectMapper = new
// ObjectMapper().registerModule(new JavaTimeModule());

// @BeforeEach
// public void setUp() {
// // JWT 토큰을 생성하기 위해 가짜 UserDetails 생성
// UserDetails mockUserDetails = User.withUsername("usertest")
// .password("password")
// .roles("USER")
// .build();

// // JWT 토큰 생성
// jwtToken = jwtUtil.generateToken(mockUserDetails, "USER", "usertest");

// // JwtUtil이 UserDetails를 반환하도록 설정
// when(jwtUtil.validateToken(any(String.class),
// any(UserDetails.class))).thenReturn(true);
// when(jwtUtil.extractUsername(any(String.class))).thenReturn("usertest");
// }

// @Test // 사용자 업적 조회
// @WithMockUser(roles = "USER")
// public void testGetAchievements() throws Exception {
// // Achievement 객체 추가
// Achievement achievement = new Achievement(1L, "First Achievement", "This is a
// test achievement",
// "Test criteria");

// // UserAchievement 객체 추가
// UserAchievement userAchievement = new UserAchievement(1L, null, achievement,
// LocalDate.now());

// // getAchievementsByUserId는 이제 UserAchievement 객체 목록을 반환하므로 이를 조정
// when(achievementService.getAchievementsByUserId(1L))
// .thenReturn(List.of(userAchievement));

// // 테스트 요청
// ResultActions resultActions = mockMvc.perform(get("/api/achievements/1")
// .header("Authorization", "Bearer " + jwtToken) // JWT 토큰을 헤더에 추가
// .contentType(MediaType.APPLICATION_JSON));

// resultActions.andExpect(status().isOk())
// .andExpect(jsonPath("$[0].achievement.id").value(achievement.getId()))
// .andExpect(jsonPath("$[0].achievement.achievementName")
// .value(achievement.getAchievementName()));
// }

// @Test // 사용자 업적 저장
// @WithMockUser(roles = "USER")
// public void testCreateUserAchievement() throws Exception {
// // Achievement 객체 추가
// Achievement achievement = new Achievement(null, "New Achievement",
// "Achievement Description",
// "New criteria");

// // UserAchievement 객체 추가
// UserAchievement userAchievement = new UserAchievement(null, new User(1L),
// achievement, LocalDate.now());

// // 업적 저장이 완료된 후 반환될 UserAchievement 객체 생성
// // UserAchievement savedUserAchievement = new UserAchievement(1L, new
// User(1L),
// // achievement,
// // LocalDate.now());

// UserAchievement savedUserAchievement = new UserAchievement(null, new
// User(2L), achievement,
// LocalDate.now());

// // saveAchievement 메소드 사용
// when(achievementService.saveAchievement(any(UserAchievement.class))).thenReturn(savedUserAchievement);

// // 테스트 요청
// ResultActions resultActions = mockMvc.perform(post("/api/achievements")
// .header("Authorization", "Bearer " + jwtToken) // JWT 토큰을 헤더에 추가
// .content(objectMapper.writeValueAsString(userAchievement))
// .contentType(MediaType.APPLICATION_JSON));

// resultActions.andExpect(status().isOk())
// .andExpect(jsonPath("$.id").value(savedUserAchievement.getId()))
// .andExpect(jsonPath("$.achievement.achievementName")
// .value(savedUserAchievement.getAchievement().getAchievementName()));
// }
// }
