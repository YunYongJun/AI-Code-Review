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

@Service
public class AIAnalysisService {

    @Autowired
    private CodeRepository codeRepository;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIAnalysisService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // 간단한 코드 분석 메서드 (예제용)
    public int analyzeCode(String code) {
        // 실제 분석 로직을 구현하거나 AI 모델과 통신하는 부분을 작성합니다.
        return (int) (Math.random() * 100); // 임의 점수 반환 예제
    }

    // 코드에 대한 피드백 생성 메서드 (예제용)
    public String generateFeedback(String code) {
        // 실제 피드백 로직을 구현하거나 AI 모델에서 반환된 피드백을 반환합니다.
        return "코드의 성능을 개선할 수 있습니다."; // 예제 피드백
    }

    // AI 모델과 상호작용하여 코드를 분석하는 메서드
    public CodeSubmission analyzeAndStoreCode(String userId, String code) throws IOException {
        // 코드 정보를 JSON으로 변환하여 AI 모델에 전송할 데이터 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userID", userId);
        requestBody.put("submittedCode", code);

        // AI 모델의 URL 설정
        String aiModelUrl = "http://192.168.34.16:8888/predict";

        // AI 모델에 요청 보내기
        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class);

        // AI의 응답을 JSON으로 파싱
        JsonNode jsonResponse = objectMapper.readTree(aiResponse);

        // 응답에서 분석 결과 추출
        String feedbackContent = jsonResponse.get("response").asText();
        LocalDate feedbackDate = LocalDate.now();

        // CodeSubmission 객체 생성 및 JSON 구조에 맞게 설정
        CodeSubmission submission = new CodeSubmission(
                Long.parseLong(userId),
                code,
                feedbackContent,
                jsonResponse.path("score").path("initialScore").asInt() // 초기 점수 설정
        );
        submission.setSubmissionDate(LocalDate.now());
        submission.setRevisedScore(jsonResponse.path("score").path("revisedScore").asInt());
        submission.setFeedbackDate(feedbackDate);

        // DB에 저장
        codeRepository.save(submission);

        return submission;
    }

    // 수정된 코드 분석 및 저장 메서드
    public CodeSubmission analyzeAndStoreRevisedCode(String userId, String revisedCode) throws IOException {
        // 수정된 코드 정보를 JSON으로 변환하여 AI 모델에 전송할 데이터 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userID", userId);
        requestBody.put("revisedCode", revisedCode);

        // AI 모델의 URL 설정 (수정된 코드 분석)
        String aiModelUrl = "http://192.168.34.16:8888/repredict";
        // AI 모델에 요청 보내기
        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class);
        // AI의 응답을 JSON으로 파싱
        JsonNode jsonResponse = objectMapper.readTree(aiResponse);
        // 응답에서 분석 결과 추출
        String feedbackContent = jsonResponse.get("response").asText();
        LocalDate feedbackDate = LocalDate.now();

        // 기존 코드 제출 기록을 가져와 수정된 내용 업데이트
        Optional<CodeSubmission> optionalSubmission = codeRepository.findByUserId(Long.parseLong(userId));

        if (optionalSubmission.isPresent()) {
            CodeSubmission submission = optionalSubmission.get();
            submission.setRevisedCode(revisedCode);
            submission.setRevisedScore(jsonResponse.path("score").path("revisedScore").asInt());
            submission.setFeedback(feedbackContent);
            submission.setFeedbackDate(feedbackDate);

            // DB에 저장
            codeRepository.save(submission);

            return submission;
        } else {
            throw new RuntimeException("해당 사용자 ID에 대한 코드 제출 기록이 없습니다.");
        }
    }
}
