package com.aicodegem.dto;

public class RevisedCodeRequest {
    private String submissionId; // 수정하려는 제출물 ID
    private String revisedCode; // 수정된 코드

    // Getters and Setters
    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getRevisedCode() {
        return revisedCode;
    }

    public void setRevisedCode(String revisedCode) {
        this.revisedCode = revisedCode;
    }
}
