package com.aicodegem.controller;

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
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder; // 생성자에서 주입
    }

    // 회원가입 API
    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    // 로그인 API
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) throws Exception {
        // 유저를 로드
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());

        // 비밀번호 확인
        if (!passwordEncoder.matches(userDTO.getPassword(), userDetails.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // 사용자 역할 가져오기
        String role = userService.getUserRole(userDTO.getUsername());

        // JWT 생성 및 반환
        final String jwtToken = jwtUtil.generateToken(userDetails, role);

        return jwtToken;
    }
}
