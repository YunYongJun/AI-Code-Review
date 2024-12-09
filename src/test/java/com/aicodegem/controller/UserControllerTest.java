package com.aicodegem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.aicodegem.service.UserService;
import com.aicodegem.dto.UserDTO;
import com.aicodegem.security.JwtUtil;
import com.aicodegem.repository.UserRepository;
import com.aicodegem.repository.RankingRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private UserRepository userRepository;

        @MockBean
        private RankingRepository rankingRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @BeforeEach
        public void setUp() {
                passwordEncoder = new BCryptPasswordEncoder();
        }

        @Test // 회원가입
        public void testRegisterUser() throws Exception {
                // Given
                UserDTO userDTO = new UserDTO("testUser", "testEmail@example.com", "password123", "1234567890");
                String responseMessage = "User and Ranking registered successfully";

                when(userService.registerUser(any(UserDTO.class))).thenReturn(responseMessage);

                // When & Then
                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"username\":\"testUser\",\"email\":\"testEmail@example.com\",\"password\":\"password123\",\"phoneNum\":\"1234567890\"}"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(responseMessage));

                verify(userService, times(1)).registerUser(any(UserDTO.class));
        }

        @Test // 로그인
        public void testLogin() throws Exception {
                // Given
                UserDTO userDTO = new UserDTO("testUser", "password123", "testEmail@example.com", "1234567890");

                // Mock PasswordEncoder를 사용하여 비밀번호를 암호화
                String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

                // UserDetails 객체 생성 (암호화된 비밀번호 포함)
                org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                                userDTO.getUsername(),
                                encodedPassword, // 암호화된 비밀번호 사용
                                new ArrayList<>());

                // Mock loadUserByUsername
                when(userService.loadUserByUsername(userDTO.getUsername())).thenReturn(userDetails);

                // Mock getUserRole
                when(userService.getUserRole(userDTO.getUsername())).thenReturn("user");

                // Mock generateToken
                when(jwtUtil.generateToken(eq(userDetails), eq("user"), eq("testUser"))).thenReturn("dummyJwtToken");

                // When & Then
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"testUser\",\"password\":\"password123\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("dummyJwtToken"));

                verify(userService, times(1)).loadUserByUsername(userDTO.getUsername());
                verify(jwtUtil, times(1)).generateToken(eq(userDetails), eq("user"), eq("testUser"));
        }

        @Test // 개인정보 수정
        public void testUpdateUserInfo() throws Exception {
                // Given
                Long userId = 1L;
                String email = "newEmail@example.com";
                String currentPassword = "password123";
                String newPassword = "newPassword123";
                String phoneNum = "0987654321";
                String responseMessage = "사용자 정보가 성공적으로 변경 되었습니다.";

                when(userService.updateUserInfo(userId, email, currentPassword, newPassword, phoneNum))
                                .thenReturn(responseMessage);

                // When & Then
                mockMvc.perform(put("/api/auth/update/{userId}", userId)
                                .param("email", email)
                                .param("currentPassword", currentPassword)
                                .param("newPassword", newPassword)
                                .param("phoneNum", phoneNum))
                                .andExpect(status().isOk())
                                .andExpect(content().string(responseMessage));

                verify(userService, times(1)).updateUserInfo(userId, email, currentPassword, newPassword, phoneNum);
        }

        @Test // 토큰 재생성
        public void testRefreshToken() throws Exception {
                // Given
                String jwtToken = "dummyJwtToken";
                when(jwtUtil.extractUsername(jwtToken)).thenReturn("testUser");
                when(jwtUtil.validateToken(eq(jwtToken), any())).thenReturn(true); // 수정된 부분
                when(jwtUtil.generateToken(any(), any(), any())).thenReturn("newDummyJwtToken");

                // When & Then
                mockMvc.perform(post("/api/auth/refresh")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("newDummyJwtToken"));

                verify(jwtUtil, times(1)).generateToken(any(), any(), any());
        }

}
