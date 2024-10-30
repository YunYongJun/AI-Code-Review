package com.aicodegem.controller;

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

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) throws Exception {
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());
        // JWT 토큰 생성 로직
        return "User logged in";
    }
}
