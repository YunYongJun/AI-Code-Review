package com.aicodegem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.aicodegem.service.UserService;
import com.aicodegem.dto.UserDTO;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO); // ResponseEntity로 반환
    }

    // 토큰 기능 로그인 기능 추가하시면 될 것 같아요.
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) throws Exception {
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());

        // JWT 토큰 생성 로직 추가
        // 로그인 기능 추가
        return ResponseEntity.ok("User logged in");
    }
}
