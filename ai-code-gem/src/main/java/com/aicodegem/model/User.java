// 사용자 엔티티
package com.aicodegrader.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")  // MongoDB에서 "users" 컬렉션과 매핑
public class User {

    @Id  // MongoDB의 _id 필드와 매핑
    private String id;
    private String username;
    private String password;
    private String email;

    // 기본 생성자 (No-args constructor)
    public User() {}

    // 모든 필드를 받는 생성자
    public User(String id, String username, String password, String email) {
        this.id = id; //몽고디비에서 자동으로 설정됨
        this.username = username; // 로그인아이디 (중복체크함)
        this.password = password;
        this.email = email;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Optional: toString() method for debugging
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
