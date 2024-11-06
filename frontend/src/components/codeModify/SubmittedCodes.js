import React, { useState } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import './SubmittedCodes.css';

const submittedCodes = [
  {
    id: 1,
    title: '테스트',
    status: '0',
    submissionTime: '2024-10-21 10:30',
    detail: 'console.log("Hello World") // 세미콜론이 없습니다. \nint n = true; // 형식이 맞지 않습니다. \n\n\n\n',
    language: 'java',
  },
  {
    id: 2,
    title: '회원가입',
    status: '2.3',
    submissionTime: '2024-10-21 11:00',
    detail: 'function nextNum(n) { return n + 1; }\n\n\n\n',
    language: 'python',
  },
  // 다른 코드들...
];

function SubmittedCodes() {
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');
  const [language, setLanguage] = useState('java'); // 언어 상태 추가

  // 언어별 확장 모드 매핑
  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  // 코드 선택 핸들러
  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    setEditedDetail(code.detail);
    setLanguage(code.language || 'java'); // 선택한 코드의 언어로 초기화
  };

  // 수정된 코드 제출 핸들러
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

    const submissionData = {
      revisedCode: editedDetail,
      language, // 언어도 함께 전송
    };

    try {
      const response = await fetch('http://192.168.34.16:8888/api/code/resubmit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(submissionData),
      });

      if (!response.ok) {
        throw new Error('수정된 코드 제출 실패');
      }

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
          {submittedCodes.map((code) => (
            <li key={code.id} onClick={() => handleCodeSelect(code)}>
              {code.title}
            </li>
          ))}
        </ul>
      </div>

      <div className="sc-code-details">
        {selectedCode ? (
          <>
            <h4>{selectedCode.title}</h4>

            {/* 언어 선택 */}
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

            {/* CodeMirror 에디터 */}
            <CodeMirror
              value={editedDetail}
              extensions={[languageExtensions[language] || java()]} // 언어가 정의되지 않은 경우 기본값으로 Java 확장을 사용
              onChange={(value) => setEditedDetail(value)}
              height="200px"
              className="sc-code-input"
            />

            <p>점수: {selectedCode.status}</p>
            <p>제출 시간: {selectedCode.submissionTime}</p>
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
