import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]);
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');
  const [language, setLanguage] = useState('java');
  const [userId, setUserId] = useState(null);
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태 추가
  const [tipIndex, setTipIndex] = useState(0); // 팁 인덱스 관리

  // 로딩 중에 표시될 팁 목록
  const tips = [
    "Tip 1: 상단 카테고리바에 있는 순위 버튼을 클릭하면 누적 점수에 따른 전체 순위를 확인할 수 있습니다.",
    "Tip 2: 상단 로그아웃 버튼 왼쪽에 있는 사용자 이름을 클릭하면 개인 정보를 수정할 수 있습니다.",
    "Tip 3: CODEREVIEW 로고를 클릭하면 메인화면으로 이동합니다.",
    "Tip 4: 상단 카테고리바에 있는 코드 제출 버튼을 클릭한 후, 코드를 입력하여 체점을 받을 수 있습니다.",
    "Tip 5: 로그인을 하지 않으면, 업적을 볼 수 없고, 체점 기능을 사용할 수 없습니다.",
  ];

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.userId);

      fetch(`http://localhost:8080/api/code/submissions?userId=${decodedToken.userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((response) => {
          if (!response.ok) throw new Error('코드 제출 목록을 불러오는 데 실패했습니다.');
          return response.json();
        })
        .then((data) => {
          setSubmittedCodes(data);
        })
        .catch((error) => console.error('Error:', error));
    }
  }, []);

  useEffect(() => {
    // 팁 인덱스를 6초 간격으로 업데이트하여 다른 팁을 표시
    const tipTimer = setInterval(() => {
      setTipIndex((prevIndex) => (prevIndex + 1) % tips.length);
    }, 6000); // 6초 간격

    return () => {
      clearInterval(tipTimer); // 팁 변경 인터벌 정리
    };
  }, [tips.length]);

  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    const codeToDisplay = code.revisedCode || code.initialCode;
    setEditedDetail(codeToDisplay);
    setLanguage(code.language || 'java');
  };

  const resubmitCode = async () => {
    if (!selectedCode) {
      alert('수정할 코드를 선택해 주세요.');
      return;
    }

    setIsLoading(true); // 로딩 시작

    // 피드백에 값이 있으면 수정된 코드 대신 피드백으로 editedDetail을 업데이트
    if (selectedCode.revisedFeedback) {
      setEditedDetail(selectedCode.revisedFeedback);
    }

    const submissionId = selectedCode.submissionId || selectedCode.id;
    const resubmissionData = {
      submissionId: submissionId,
      revisedCode: editedDetail,
    };

    try {
      const response = await fetch('http://localhost:8080/api/code/submit/revised', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(resubmissionData),
      });

      if (!response.ok) throw new Error('수정된 코드 제출 실패');

      const updatedSubmission = await response.json();
      setSelectedCode(updatedSubmission); // 수정된 코드 데이터 반영
      alert('수정된 코드가 성공적으로 제출되었습니다.');
    } catch (error) {
      console.error('Error:', error);
      alert('코드 제출 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false); // 로딩 종료
    }
  };

  const calculateAverageScore = (feedback) => {
    const scores = feedback.match(/:\s*([\d.]+)\/10/g);
    if (!scores) return null;

    const totalScore = scores.reduce((sum, scoreText) => {
      const score = parseFloat(scoreText.match(/([\d.]+)\/10/)[1]);
      return sum + score;
    }, 0);

    return (totalScore / scores.length).toFixed(1);
  };

  const formatFeedback = (feedback) => {
    if (!feedback) return '아직 피드백이 없습니다.';

    const formattedFeedback = feedback
      .replace('###Instruction### 코드 스니펫이 명시한 항목의 원칙을 잘 따르고 있는지 판단하십시오, 평가 사항은 각 항목당 10점 만점으로 숫자와 함께 점수를 표기 하십시오.', '')
      .replace('1. 가독성:', '\n1. 가독성:')
      .replace('2. 간결함:', '\n2. 간결함:')
      .replace('3. 명확한 의도:', '\n3. 명확한 의도:')
      .replace('4. 중복 최소화:', '\n4. 중복 최소화:')
      .replace('5. 적절한 주석:', '\n5. 적절한 주석:')
      .replace('6. 작고 집중된 함수:', '\n6. 작고 집중된 함수:')
      .replace('7. 일관성:', '\n7. 일관성:')
      .replace('8. 적절한 오류 처리:', '\n8. 적절한 오류 처리:')
      .replace('###결론###', '\n###결론###');

    const averageScore = calculateAverageScore(formattedFeedback);
    if (averageScore) selectedCode.initialScore = averageScore;

    return formattedFeedback;
  };

  return (
    <div className="sc-submitted-codes-page">
      <div className={`sc-code-list ${isLoading ? 'scp-blur' : ''}`}>
        <h3>제출 코드 목록</h3>
        <ul>
          {submittedCodes.map((code, index) => (
            <li key={index} onClick={() => handleCodeSelect(code)}>
              {code.title || `제출 코드 ${index + 1}`}
            </li>
          ))}
        </ul>
      </div>

      <div className="sc-code-details">
        {selectedCode ? (
          <>
            <h4>{selectedCode.title}</h4>
            <div className="scp-form-group">
              <label htmlFor="language-select">언어</label>
              <select
                id="language-select"
                value={language}
                onChange={(e) => setLanguage(e.target.value)}
                className="scp-select-input"
              >
                <option value="java">Java</option>
                <option value="python">Python</option>
                <option value="cpp">C++</option>
              </select>
            </div>

            <CodeMirror
              value={editedDetail}
              extensions={[languageExtensions[language] || java()]}
              onChange={(value) => setEditedDetail(value)}
              height="400px"
            />

            <div className="sc-feedback-section">
              <h5>
                {selectedCode.revisedFeedback
                  ? 'AI 피드백 (수정 후 결과)'
                  : 'AI 피드백'}
              </h5>
              <pre>
                {selectedCode.revisedFeedback
                  ? formatFeedback(selectedCode.revisedFeedback)
                  : formatFeedback(selectedCode.feedback)}
              </pre>
            </div>

            <p>초기 점수: {selectedCode.initialScore || 'N/A'}</p>
            <p>수정 후 점수: {selectedCode.revisedScore || 'N/A'}</p>

            <button onClick={resubmitCode}>수정된 코드 제출</button>
          </>
        ) : (
          <p>코드를 선택해 주세요.</p>
        )}
      </div>

      {isLoading && (
        <div className="scp-loading-overlay">
          <div className="scp-loading-spinner"></div> {/* 로딩 애니메이션 */}
          <div className="scp-tip-container">
            <p>{tips[tipIndex]}</p> {/* 로딩 중 팁 표시 */}
          </div>
        </div>
      )}
    </div>
  );
}

export default SubmittedCodes;
