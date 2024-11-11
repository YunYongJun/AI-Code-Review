import React, { useState, useEffect } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import { jwtDecode } from 'jwt-decode';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]); // API에서 가져온 코드 제출 목록을 저장
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');
  const [language, setLanguage] = useState('java');
  const [userId, setUserId] = useState(null); // 사용자 ID 상태 추가

  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  // 컴포넌트 마운트 시 사용자 ID 설정 및 API 호출
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.userId); // 사용자 ID 설정

      // 사용자 코드 제출 목록 가져오기
      fetch('http://localhost:8080/api/code/submissions', {
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

  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    setEditedDetail(code.feedback || ''); // 선택한 코드의 피드백 내용을 코드 에디터에 표시
    setLanguage(code.language || 'java');
  };

  const resubmitCode = async () => {
    if (!selectedCode) {
      alert('수정할 코드를 선택해 주세요.');
      return;
    }

    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const submissionData = {
      revisedCode: editedDetail,
      language,
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

      if (!response.ok) throw new Error('수정된 코드 제출 실패');
      alert('수정된 코드가 제출되었습니다.');
    } catch (error) {
      console.error('Error:', error);
      alert('수정된 코드 제출 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="sc-submitted-codes-page">
      <div className="sc-code-list">
        <h3>제출 코드 목록</h3>
        <ul>
          {submittedCodes.map((code, index) => (
            <li key={index} onClick={() => handleCodeSelect(code)}>
              {index + 1}
            </li>
          ))}
        </ul>
      </div>

      <div className="sc-code-details">
        {selectedCode ? (
          <>
            <h4>{`제출 코드 ${submittedCodes.indexOf(selectedCode) + 1}`}</h4>
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
