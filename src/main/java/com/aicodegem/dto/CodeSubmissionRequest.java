package com.aicodegem.dto;

public class CodeSubmissionRequest {
    private Long userId;
    private String code;
    private String title; // 코드 제목 필드

    public CodeSubmissionRequest(Long userId, String code, String title) {
        this.userId = userId;
        this.code = code;
        this.title = title;
    }

    public CodeSubmissionRequest() {
    }

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
