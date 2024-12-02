public interface CodeSubmissionService {
    /** 코드 제출 */
    CodeSubmission submitCode(Long userId, String code, String title);
    
    /** 코드 재제출 */
    CodeSubmission reviseCode(String submissionId, String revisedCode);
    
    /** 제출된 코드 목록 가져오기 */
    List<CodeSubmission> getUserSubmissions(Long userId);
}
