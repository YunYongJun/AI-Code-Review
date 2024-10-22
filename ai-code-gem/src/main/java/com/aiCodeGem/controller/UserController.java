package com.aicodegem.controller;

import com.aicodegem.dto.UserDTO;
import com.aicodegem.service.UserService;
import com.aicodegem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) throws Exception {
        try {
            // 인증 처리
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        } catch (Exception e) {
            throw new Exception("Invalid username or password");
        }

        // 사용자 정보를 기반으로 JWT 토큰 발급
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());
        return jwtUtil.generateToken(userDetails.getUsername());
    }
}
