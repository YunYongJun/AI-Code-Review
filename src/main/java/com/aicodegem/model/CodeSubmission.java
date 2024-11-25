package com.aicodegem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "codeSubmissions")
public class CodeSubmission {

    @Id
    private String id; // mongodb는 string 값으로 설정

    private Long userId; // 사용자 id
    private String initialCode; // 초기 제출된 코드
    private String title; // 제목
    private LocalDate submissionDate; // 제출일자
    private String pylintOutput; // 제출 후 나온 피드백

    // 추가된 필드
    private String feedback; // 피드백 내용
    private int initialScore; // 초기 점수
    private String revisedCode; // 수정 후 코드
    private int revisedScore; // 수정 후 점수
    private String revisedFeedback; // 수정후 피드백 내용
    private LocalDate feedbackDate; // 피드백 추가 일자

    // @SuppressWarnings("unused")
    private String revisedPylintOutput; // 추가된 필드

    // 생성자
    public CodeSubmission(Long userId, String initialCode, String title) {
        this.userId = userId;
        this.initialCode = initialCode;
        this.title = title;
        this.submissionDate = LocalDate.now();
    }

    // Getters and Setters
    public String getRevisedPylintOutput() {
        return revisedPylintOutput;
    }

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

    public String getPylintOutput() {
        return pylintOutput;
    }

    public void setPylintOutput(String pylintOutput) {
        this.pylintOutput = pylintOutput;
    }

    // 추가된 Getter & Setter
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

    public String getRevisedCode() {
        return revisedCode;
    }

    public void setRevisedCode(String revisedCode) {
        this.revisedCode = revisedCode;
    }

    public int getRevisedScore() {
        return revisedScore;
    }

    public void setRevisedScore(int revisedScore) {
        this.revisedScore = revisedScore;
    }

    public String getRevisedFeedback() {
        return revisedFeedback;
    }

    public void setRevisedFeedback(String revisedFeedback) {
        this.revisedFeedback = revisedFeedback;
    }

    public void setRevisedPylintOutput(String revisedPylintOutput) {
        this.revisedPylintOutput = revisedPylintOutput;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}
