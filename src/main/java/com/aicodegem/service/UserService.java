package com.aicodegem.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.aicodegem.model.User;
import com.aicodegem.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.aicodegem.dto.UserDTO;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    // 사용자 역할을 가져오는 메서드
    public String getUserRole(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(User::getRole).orElse("user"); // 기본 역할을 "user"로 설정
    }

    // 유저 등록 로직
    public String registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalStateException("Username already taken.");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(encodedPassword);
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNum(userDTO.getPhoneNum());
        newUser.setRole("user"); // 기본 역할을 "user"로 설정

        userRepository.save(newUser);
        return "User registered successfully";
    }

    public String updateUserInfo(Long userId, String email, String currentPassword, String newPassword,
            String phoneNum) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (user == null) {
            return "사용자를 찾을 수 없습니다.";
        }

        // 현재 비밀번호 확인 (비밀번호 인코딩을 고려한 비교)
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "현재 비밀번호가 잘못 되었습니다.";
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword)); // 새 비밀번호를 인코딩하여 저장
        user.setPhoneNum(phoneNum);
        userRepository.save(user);

        return "사용자 정보가 성공적으로 변경 되었습니다.";
    }

    // user_id로 user_name 가져오기
    public Optional<String> getUsernameById(Long userId) {
        return userRepository.findUsernameById(userId);
    }

}
