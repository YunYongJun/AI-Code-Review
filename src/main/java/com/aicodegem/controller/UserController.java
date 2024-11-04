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

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) throws Exception {
        // 유저를 로드
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());

        // 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(userDTO.getPassword(), userDetails.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // 사용자 역할 가져오기
        String role = userService.getUserRole(userDTO.getUsername());

        // JWT 생성 및 반환
        final String jwtToken = jwtUtil.generateToken(userDetails, role);

        return jwtToken; // 클라이언트에 JWT 반환
    }

    // 사용자 정보 수정 엔드포인트
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String phoneNum) {

        String result = userService.updateUserInfo(userId, email, currentPassword, newPassword, phoneNum);
        return ResponseEntity.ok(result);
    }

    // user_id로 user_name을 가져오는 엔드포인트
    @GetMapping("/name/{userId}")
    public ResponseEntity<String> getUsernameById(@PathVariable Long userId) {
        return userService.getUsernameById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
