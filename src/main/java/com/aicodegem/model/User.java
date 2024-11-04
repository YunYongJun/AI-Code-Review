// 사용자 엔티티
package com.aicodegem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") // MongoDB에서 "users" 컬렉션과 매핑
public class User {

    @Id // MongoDB의 _id 필드와 매핑
    private String id;
    private String username;
    private String password;
    private String email;
    private String phoneNum;
    private String role; // 사용자의 역할 필드 추가 (예: "user", "admin")

    // 기본 생성자
    public User() {
    }

    // 모든 필드를 받는 생성자 (id는 MongoDB에서 자동으로 설정)
    public User(String username, String password, String email, String phoneNum, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
        this.role = role;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Optional: toString() method for debugging
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
