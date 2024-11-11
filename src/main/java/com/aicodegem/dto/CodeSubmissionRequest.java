package com.aicodegem.dto;

public class CodeSubmissionRequest {
    private Long userId; // 사용자 ID
    private String code; // 제출된 코드

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}