public interface AIAnalysisService {
    /** 코드 분석(제출) */
    int analyzeCode(String code);
    
    /** 피드백 생성 */
    String generateFeedback(String code);
    
    /** 코드 db 저장 */
    CodeSubmission analyzeAndStoreCode(String userId, String code, String title);
    
    /** 분석된 코드 재제출*/
    // CodeSubmission analyzeAndStoreRevisedCode(String submissionId, String revisedCode)
}