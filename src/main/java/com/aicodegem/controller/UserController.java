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

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) throws Exception {
        // 유저를 로드
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());

        // 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(userDTO.getPassword(), userDetails.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // 사용자 역할 가져오기
        String role = userService.getUserRole(userDTO.getUsername());
        String userId = userService.getUserId(userDTO.getUsername());
        // JWT 생성
        final String jwtToken = jwtUtil.generateToken(userDetails, role, userId);

        // JSON 형식으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response); // JSON 형식으로 응답
    }
}
