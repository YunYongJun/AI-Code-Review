package com.aicodegem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.aicodegem.service.UserService;
import com.aicodegem.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.aicodegem.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) throws Exception {
        // 유저를 로드
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());

        // 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(userDTO.getPassword(), userDetails.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // 사용자 역할과 ID 가져오기
        String role = userService.getUserRole(userDTO.getUsername());
        String username = userDTO.getUsername();
        // JWT 생성
        final String jwtToken = jwtUtil.generateToken(userDetails, role, username);

        // JSON 형식으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response); // JSON 형식으로 응답
    }

    // 토큰 리프레시 엔드포인트
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String token) {
        // 토큰에서 Bearer 부분 제거
        String jwt = token.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userService.loadUserByUsername(username);

        // 토큰이 유효한 경우 새로운 토큰 생성 및 반환
        if (jwtUtil.validateToken(jwt, userDetails)) {
            String newToken = jwtUtil.generateToken(userDetails, jwtUtil.extractRole(jwt),
                    jwtUtil.extractUsername(jwt));
            Map<String, String> response = new HashMap<>();
            response.put("token", newToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    // 사용자 정보 수정 엔드포인트
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String phoneNum) {

        // 사용자 정보 업데이트 결과 반환
        String result = userService.updateUserInfo(userId, email, currentPassword, newPassword, phoneNum);
        return ResponseEntity.ok(result);
    }
}
