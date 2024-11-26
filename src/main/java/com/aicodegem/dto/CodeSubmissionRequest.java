package com.aicodegem.dto;

public class CodeSubmissionRequest {
    private Long userId;
    private String code;
    private String title;

    // 기본 생성자 (필요한 경우)
    public CodeSubmissionRequest() {
    }

    // 파라미터를 받는 생성자
    public CodeSubmissionRequest(long userId, String code, String title) {
        this.userId = userId;
        this.code = code;
        this.title = title;
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
