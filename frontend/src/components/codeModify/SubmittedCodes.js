import React, { useState, useEffect } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import { jwtDecode } from 'jwt-decode';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]);
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');
  const [language, setLanguage] = useState('java');
  const [userId, setUserId] = useState(null);

  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

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
        .then((data) => setSubmittedCodes(data))
        .catch((error) => console.error('Error:', error));
    }
  }, []);

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

    const resubmissionData = {
      userId,
      revisedCode: editedDetail,
    };

    try {
      const response = await fetch('http://localhost:8080/api/code/revise', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(resubmissionData),
      });

      if (!response.ok) throw new Error('수정된 코드 제출 실패');
      alert('수정된 코드가 제출되었습니다.');
    } catch (error) {
      console.error('Error:', error);
      alert('코드 제출 중 오류가 발생했습니다.');
    }
  };

  const calculateAverageScore = (feedback) => {
    const scores = feedback.match(/:\s*([\d.]+)\/10/g);
    if (!scores) return null;

    const totalScore = scores.reduce((sum, scoreText) => {
      const score = parseFloat(scoreText.match(/([\d.]+)\/10/)[1]);
      return sum + score;
    }, 0);

    return (totalScore / scores.length).toFixed(1); // 평균 점수 계산, 소수점 1자리
  };

  const formatFeedback = (feedback) => {
    if (!feedback) return '아직 피드백이 없습니다.';

    // 항목별 줄바꿈 처리
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
    if (averageScore) selectedCode.initialScore = averageScore; // 초기 점수 갱신

    return formattedFeedback;
  };

  return (
    <div className="sc-submitted-codes-page">
      <div className="sc-code-list">
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
              height="200px"
              className="sc-code-input"
            />

            <div className="sc-feedback-section">
              <h5>AI 피드백</h5>
              <pre>{formatFeedback(selectedCode.feedback)}</pre>
            </div>

            <p>초기 점수: {selectedCode.initialScore}</p>
            <p>수정 후 점수: {selectedCode.revisedScore}</p>

            <button onClick={resubmitCode}>수정된 코드 제출</button>
          </>
        ) : (
          <p>코드를 선택해 주세요.</p>
        )}
      </div>
    </div>
  );
}

export default SubmittedCodes;
