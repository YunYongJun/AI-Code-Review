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
    @Autowired
    private RankingService rankingService;

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
        List<CodeSubmission> submissions = codeRepository.findAllByUserId(userId);
        logger.info("사용자 ID {}의 제출 코드 개수: {}", userId, submissions.size()); // 제출 코드 개수 로그
        return submissions;
    }

    /**
     * 특정 제출 ID로 제출 기록을 조회
     * 
     * @param submissionId 제출 ID
     * @return 코드 제출 정보
     */
    public CodeSubmission getSubmissionById(String submissionId) {
        logger.info("제출 코드 조회 요청: submissionId={}", submissionId); // 로그 추가
        Optional<CodeSubmission> submission = codeRepository.findById(submissionId);
        if (submission.isPresent()) {
            logger.info("제출 코드 조회 성공: {}", submissionId); // 성공 로그
        } else {
            logger.warn("제출 코드 조회 실패: {} - 해당 ID의 제출 코드가 존재하지 않습니다.", submissionId); // 실패 로그
        }
        return submission.orElse(null);
    }

    /**
     * 최초 코드 제출 처리 (AI 분석 후 DB에 저장)
     */
    public CodeSubmission submitCode(Long userId, String code, String title) throws IOException {
        logger.info("사용자 ID: {}의 코드 제출 요청이 들어왔습니다. 제목: {}", userId, title); // 로그 추가

        // 1. 먼저 코드 정보를 DB에 저장 (AI 분석 이전 상태)
        CodeSubmission submission = new CodeSubmission(userId, code, title); // title 포함
        submission.setSubmissionDate(LocalDate.now());

        logger.info("코드 제출 정보를 DB에 저장합니다. 제출 날짜: {}", submission.getSubmissionDate()); // 로그 추가

        // MongoDB에 제출된 코드만 저장 (AI 분석 이전 상태)
        submission = codeRepository.save(submission); // 초기 코드 저장
        logger.info("코드가 DB에 저장되었습니다. 제출 ID: {}", submission.getId()); // 저장된 제출 ID 로그

        // 2. AI 모델에 코드 분석 요청
        String aiModelUrl = "http://192.168.34.13:8888/predict"; // AI 모델 URL
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("submittedCode", code);

        // AI 모델에 요청 보내고, 응답 받기
        logger.info("AI 모델에 코드 분석 요청을 보냅니다."); // 로그 추가
        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class); // AI 모델 응답 받기
        logger.info("AI 모델 응답: {}", aiResponse); // AI 응답 로그

        // 3. AI 분석 결과 처리
        JsonNode jsonResponse = objectMapper.readTree(aiResponse); // AI 응답을 JSON으로 변환
        String feedbackContent = jsonResponse.get("response").asText(); // 피드백 내용 추출
        int score = 6000;
        // int score = jsonResponse.path("score").asInt(); // 점수 추출
        logger.info("AI 분석 결과 - 피드백: {}, 점수: {}", feedbackContent, score); // AI 분석 결과 로그

        // 4. 분석 결과를 기존 submission에 반영
        submission.setFeedback(feedbackContent); // AI 피드백 저장
        submission.setInitialScore(score); // 초기 점수 저장
        submission.setRevisedScore(score); // 수정된 점수(초기와 동일할 경우 동일하게 처리)
        submission.setFeedbackDate(LocalDate.now()); // 피드백 날짜 저장

        // 5. DB에 저장된 제출 정보 업데이트 (AI 분석 결과 반영)
        logger.info("AI 분석 결과를 DB에 반영하여 제출 정보를 업데이트합니다."); // 로그 추가

        // 6. Ranking의 totalScore 업데이트
        rankingService.updateTotalScore(userId, score); // RankingService를 호출하여 점수 업데이트
        logger.info("Ranking에 점수 업데이트 완료 - 점수: {}", score);

        return codeRepository.save(submission); // 분석 결과 반영 후 업데이트된 코드 저장

    }

    // 코드 수정
    public CodeSubmission analyzeAndStoreRevisedCode(String submissionId, String revisedCode) throws IOException {
        logger.info("제출 ID {}에 대한 코드 수정 요청이 들어왔습니다.", submissionId);
        Optional<CodeSubmission> optionalSubmission = codeRepository.findById(submissionId);

        if (optionalSubmission.isEmpty()) {
            logger.error("제출 ID '{}'에 해당하는 기록이 없습니다.", submissionId); // 에러 로그 추가
            throw new RuntimeException("제출 ID에 해당하는 기록이 없습니다.");
        }

        CodeSubmission submission = optionalSubmission.get();

        // AI 분석 요청
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("revisedCode", revisedCode);

        String aiModelUrl = "http://192.168.34.13:8888/repredict";
        logger.info("AI 모델에 수정된 코드 분석 요청을 보냅니다.");
        String aiResponse = restTemplate.postForObject(aiModelUrl, requestBody, String.class);

        JsonNode jsonResponse = objectMapper.readTree(aiResponse);
        String feedbackContent = jsonResponse.path("feedback").asText();
        int revisedScore = jsonResponse.path("score").asInt();

        // 기존 제출 정보 업데이트
        submission.setRevisedCode(revisedCode);
        submission.setRevisedFeedback(feedbackContent);
        submission.setRevisedScore(revisedScore);
        submission.setFeedbackDate(LocalDate.now());

        logger.info("수정된 AI 분석 결과를 DB에 반영하여 제출 정보를 업데이트합니다.");

        // Ranking의 totalScore 업데이트
        rankingService.updateTotalScore(submission.getUserId(), revisedScore); // RankingService를 호출하여 점수 업데이트
        logger.info("Ranking에 점수 업데이트 완료 - 점수: {}", revisedScore);

        return codeRepository.save(submission);
    }
}
