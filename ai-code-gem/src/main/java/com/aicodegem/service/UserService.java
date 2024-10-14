//회원가입 관련 비지니스 로직
package com.aicodegrader.service;

import com.aicodegrader.dto.UserDTO;
import com.aicodegrader.model.User;
import com.aicodegrader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(UserDTO userDTO) {
        // 중복된 사용자 확인
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return "Username is already taken";
        }

        // 비밀번호 암호화
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
        return "User registered successfully";
    }
}
