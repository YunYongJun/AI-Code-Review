<<<<<<< HEAD
=======

>>>>>>> 3c4ddb7dd3d1602d57d79ea05a316b253166a909
package com.aicodegem.service;

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

    public String registerUser(UserDTO userDTO) {
        // 유저가 이미 존재하는지 확인
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return "User with this username already exists.";
        }

        // User 객체 생성
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword()); // 비밀번호 해시화하지 않음
        user.setEmail(userDTO.getEmail());
        user.setPhoneNum(userDTO.getPhoneNum());

        // 사용자 정보 저장
        userRepository.save(user);

        return "User registered successfully.";
    }
}
