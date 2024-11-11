package com.aicodegem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "codeSubmissions")
public class CodeSubmission {

    @Id
    private String id;
    private Long userId;
    private String initialCode;
    private String revisedCode;
    private String feedback;
    private int initialScore;
    private int revisedScore;

    // 생성자
    public CodeSubmission(Long userId, String initialCode, String feedback, int initialScore) {
        this.userId = userId;
        this.initialCode = initialCode;
        this.feedback = feedback;
        this.initialScore = initialScore;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public String getRevisedCode() {
        return revisedCode;
    }

    public void setRevisedCode(String revisedCode) {
        this.revisedCode = revisedCode;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getInitialScore() {
        return initialScore;
    }

    public void setInitialScore(int initialScore) {
        this.initialScore = initialScore;
    }

    public int getRevisedScore() {
        return revisedScore;
    }

    public void setRevisedScore(int revisedScore) {
        this.revisedScore = revisedScore;
    }
}
