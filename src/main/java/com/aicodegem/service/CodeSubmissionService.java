package com.aicodegem.service;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@Service
public class CodeSubmissionService {

    @Autowired
    private CodeRepository codeRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 사용자 ID로 모든 제출 기록을 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 코드 제출 목록
     */
    public List<CodeSubmission> getAllSubmissionsByUserId(Long userId) {
        return codeRepository.findAllByUserId(userId);
    }

    /**
     * 특정 제출 ID로 제출 기록을 조회
     *
     * @param submissionId 제출 ID
     * @return 코드 제출 정보
     */
    public CodeSubmission getSubmissionById(String submissionId) {
        Optional<CodeSubmission> submission = codeRepository.findById(submissionId);
        return submission.orElse(null);
    }

    /**
     * 최초 코드 제출 처리 (AI 분석 후 DB에 저장)
     */
    public CodeSubmission submitCode(Long userId, String code, String title) throws IOException {
        CodeSubmission submission = new CodeSubmission(userId, code, title);
        submission.setSubmissionDate(LocalDate.now());

        submission = codeRepository.save(submission);

        String aiModelUrl = "http://192.168.34.13:8888/predict";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("submittedCode", code);

        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class);

        JsonNode jsonResponse = objectMapper.readTree(aiResponse);
        String feedbackContent = jsonResponse.get("feedback").asText();
        int score = jsonResponse.path("score").asInt();

        submission.setFeedback(feedbackContent);
        submission.setInitialScore(score);
        submission.setFeedbackDate(LocalDate.now());

        return codeRepository.save(submission);
    }

    /**
     * 수정된 코드 제출 처리 (AI 분석 후 DB 업데이트 및 결과 반환)
     */
    public Map<String, Object> analyzeAndStoreRevisedCode(Long userId, String revisedCode) throws IOException {
        Optional<CodeSubmission> existingSubmission = codeRepository.findByUserId(userId);

        if (existingSubmission.isEmpty()) {
            throw new RuntimeException("해당 사용자 ID에 대한 코드 제출 기록이 없습니다.");
        }

        CodeSubmission submission = existingSubmission.get();

        String aiModelUrl = "http://192.168.34.13:8888/repredict";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("revisedCode", revisedCode);

        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class);

        JsonNode jsonResponse = objectMapper.readTree(aiResponse);
        String feedbackContent = jsonResponse.path("feedback").asText();
        int revisedScore = jsonResponse.path("score").asInt();

        submission.setRevisedCode(revisedCode);
        submission.setRevisedFeedback(feedbackContent);
        submission.setRevisedScore(revisedScore);
        submission.setFeedbackDate(LocalDate.now());

        codeRepository.save(submission);

        // 평가 결과 반환용 데이터 생성
        Map<String, Object> result = new HashMap<>();
        result.put("revisedScore", revisedScore);
        result.put("revisedFeedback", feedbackContent);
        result.put("submissionId", submission.getId());
        return result;
    }
}
