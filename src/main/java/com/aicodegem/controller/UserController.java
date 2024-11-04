package com.aicodegem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.aicodegem.service.UserService;
import com.aicodegem.dto.UserDTO;
import com.aicodegem.security.JwtUtil;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO); // ResponseEntity로 반환
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) throws Exception {
        // 사용자 인증 시도
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        // 사용자 로드
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());

        // JWT 토큰 생성
        final String jwtToken = jwtUtil.generateToken(userDetails);

        // 응답으로 토큰 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }

    // 사용자 정보 수정 엔드포인트
    @PutMapping("/{userId}/update")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String phoneNum) {

        String result = userService.updateUserInfo(userId, email, currentPassword, newPassword, phoneNum);
        return ResponseEntity.ok(result);
    }
}
