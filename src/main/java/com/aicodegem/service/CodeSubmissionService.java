package com.aicodegem.service;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CodeSubmissionService.class);

    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private AIAnalysisService aiAnalysisService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 사용자 ID로 모든 제출 기록을 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자 코드 제출 목록
     */
    public List<CodeSubmission> getAllSubmissionsByUserId(Long userId) {
        logger.info("사용자 ID {}의 모든 제출 기록을 조회합니다.", userId);
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
        // 1. 먼저 코드 정보를 DB에 저장 (AI 분석 이전 상태)
        CodeSubmission submission = new CodeSubmission(userId, code, title); // title 포함
        submission.setSubmissionDate(LocalDate.now());

        // MongoDB에 제출된 코드만 저장 (AI 분석 이전 상태)
        submission = codeRepository.save(submission); // 초기 코드 저장

        // 2. AI 모델에 코드 분석 요청
        String aiModelUrl = "http://192.168.34.13:8888/predict"; // AI 모델 URL
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("submittedCode", code);

        // AI 모델에 요청 보내고, 응답 받기
        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class); // AI 모델 응답 받기
        System.out.println(aiResponse);

        // 3. AI 분석 결과 처리
        JsonNode jsonResponse = objectMapper.readTree(aiResponse); // AI 응답을 JSON으로 변환
        String feedbackContent = jsonResponse.get("response").asText(); // 피드백 내용 추출
        int score = jsonResponse.path("score").asInt(); // 점수 추출

        // 4. 분석 결과를 기존 submission에 반영
        submission.setFeedback(feedbackContent); // AI 피드백 저장
        submission.setInitialScore(score); // 초기 점수 저장
        submission.setRevisedScore(score); // 수정된 점수(초기와 동일할 경우 동일하게 처리)
        submission.setFeedbackDate(LocalDate.now()); // 피드백 날짜 저장

        // 5. DB에 저장된 제출 정보 업데이트 (AI 분석 결과 반영)
        return codeRepository.save(submission); // 분석 결과 반영 후 업데이트된 코드 저장
    }
}
