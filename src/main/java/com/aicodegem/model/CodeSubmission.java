package com.aicodegem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "codeSubmissions")
public class CodeSubmission {

    @Id
    private String id;

    private Long userId;
    private String initialCode;
    private String title; // 코드 제목 필드
    private LocalDate submissionDate;
    private String feedback;
    private int initialScore;
    private int revisedScore;
    private String feedbackId;
    private LocalDate feedbackDate;
    private String revisedCode;
    private String revisedFeedback;

    // 생성자 (점수와 피드백은 AI 분석 후 업데이트)
    public CodeSubmission(Long userId, String initialCode, String title) {
        this.userId = userId;
        this.initialCode = initialCode;
        this.title = title;
        this.submissionDate = LocalDate.now();
    }

    public String getId() {
        return id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
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

    public String getRevisedCode() {
        return revisedCode;
    }

    public void setRevisedCode(String revisedCode) {
        this.revisedCode = revisedCode;
    }

    public String getRevisedFeedback() {
        return revisedFeedback;
    }

    public void setRevisedFeedback(String revisedFeedback) {
        this.revisedFeedback = revisedFeedback;
    }
}
