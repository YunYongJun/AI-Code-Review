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
    private String title;
    private LocalDate submissionDate;
    private String pylintOutput;
    private String revisedCode;
    private String revisedPylintOutput;
    private LocalDate feedbackDate;

    public CodeSubmission(Long userId, String initialCode, String title) {
        this.userId = userId;
        this.initialCode = initialCode;
        this.title = title;
        this.submissionDate = LocalDate.now();
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

    public String getRevisedCode() {
        return revisedCode;
    }

    public void setRevisedCode(String revisedCode) {
        this.revisedCode = revisedCode;
    }

    public String getRevisedPylintOutput() {
        return revisedPylintOutput;
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
