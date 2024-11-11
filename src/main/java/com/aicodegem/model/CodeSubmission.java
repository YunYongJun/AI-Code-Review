package com.aicodegem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "codeSubmissions")
public class CodeSubmission {

    @Id
    private String id; // MongoDB에서 자동 생성되는 _id 필드

    private Long userId;
    private String initialCode;
    private String revisedCode;
    private String feedback;
    private int initialScore;
    private int revisedScore;
    private LocalDate submissionDate;
    private String feedbackId;
    private LocalDate feedbackDate;

    // 생성자
    public CodeSubmission(Long userId, String initialCode, String feedback, int initialScore) {
        this.userId = userId;
        this.initialCode = initialCode;
        this.feedback = feedback;
        this.initialScore = initialScore;
        this.submissionDate = LocalDate.now();
    }

    // Getter와 Setter
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

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}
