//회원가입api 컨트롤러
package com.aicodegrader.controller;

import com.aicodegrader.dto.UserDTO;
import com.aicodegrader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }
}

// UserController.java
@PostMapping("/login")
public String login(@RequestBody UserDTO userDTO) throws Exception {
    // 인증 처리 (username, password)
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
    } catch (Exception e) {
        throw new Exception("Invalid username or password");
    }

    // 사용자 정보를 기반으로 JWT 토큰 발급
    final UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUsername());
    return jwtUtil.generateToken(userDetails.getUsername());
}
