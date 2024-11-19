package com.aicodegem.dto;

public class CodeSubmissionRequest {
    private Long userId;
    private String code;
    private String title; // 코드 제목 필드

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

    public String getTitle() { 
        return title; 
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
