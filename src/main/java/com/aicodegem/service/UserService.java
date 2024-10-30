package com.aicodegem.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.aicodegem.model.User;
import com.aicodegem.repository.UserRepository;
import com.aicodegem.dto.UserDTO;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public ResponseEntity<String> registerUser(UserDTO userDTO) {
        // 유저가 이미 존재하는지 확인
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type 명시
            return ResponseEntity.badRequest()
                    .headers(headers)
                    .body("{\"message\": \"User with this username already exists.\"}");
        }

        // User 객체 생성 및 저장
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getPhoneNum());
        userRepository.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type 명시
        return ResponseEntity.ok()
                .headers(headers)
                .body("{\"message\": \"User registered successfully.\"}");
    }
}
