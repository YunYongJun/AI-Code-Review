import React, { useState, useEffect } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import { jwtDecode } from 'jwt-decode';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]); // 제출된 코드 목록
  const [selectedCode, setSelectedCode] = useState(null); // 선택된 코드
  const [editedDetail, setEditedDetail] = useState(''); // 수정된 코드
  const [language, setLanguage] = useState('java'); // 언어
  const [userId, setUserId] = useState(null); // 사용자 ID

  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  // 페이지 로딩 시 제출된 코드 목록을 불러옵니다.
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.userId);

      // 제출된 코드 목록 API 요청
      fetch(`http://localhost:8080/api/code/submissions?userId=${decodedToken.userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
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

  // 코드 선택 시 해당 코드로 내용 업데이트
  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    // revisedCode가 없으면 initialCode를 사용하도록 설정
    const codeToDisplay = code.revisedCode || code.initialCode;
    setEditedDetail(codeToDisplay);  // editedDetail에 코드 설정
    setLanguage(code.language || 'java'); // 언어 설정
  };

  // 코드 수정 후 제출 처리
  const resubmitCode = async () => {
    if (!selectedCode) {
      alert('수정할 코드를 선택해 주세요.');
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const resubmissionData = {
      userId,
      revisedCode: editedDetail, // 수정된 코드 제출
    };

    try {
      const response = await fetch('http://localhost:8080/api/code/revise', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
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

            {/* AI 피드백 영역 */}
            <div className="sc-feedback-section">
              <h5>AI 피드백</h5>
              <p>{selectedCode.feedback || '아직 피드백이 없습니다.'}</p>
            </div>

            {/* 점수 정보 */}
            <p>초기 점수: {selectedCode.initialScore}</p>
            <p>수정 후 점수: {selectedCode.revisedScore}</p>

            {/* 수정된 코드 제출 버튼 */}
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
