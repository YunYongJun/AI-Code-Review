import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import './SubmitCodePage.css';

function SubmitCodePage() {
  const [language, setLanguage] = useState('java');
  const [sourceCode, setSourceCode] = useState('');
  const [title, setTitle] = useState('');
  const [userId, setUserId] = useState('');

  // JWT 토큰에서 사용자 ID 추출
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.userId);
    }
  }, []);

  // 언어별 확장 모드 매핑
  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  const handleSubmit = async () => {
    console.log('제출된 제목:', title);
    console.log('제출된 사용자 ID:', userId);

    const formattedCode = sourceCode.replace(/\n/g, '\\n'); // 줄바꿈을 \n으로 통일
    console.log('제출된 소스 코드:', formattedCode);

    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const submissionData = {
      submittedCode: formattedCode,
      submissionDate: new Date().toISOString().split('T')[0],
      userId: userId,
    };

    try {
      const response = await fetch('http://localhost:8080/api/code/submit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(submissionData),
      });

      if (!response.ok) {
        throw new Error('제출 실패');
      }

      window.location.href = '/grading';
    } catch (error) {
      console.error('Error:', error);
      alert('제출 중 오류가 발생했습니다.');
    }
  };

  const handleListClick = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }
    window.location.href = '/submitted-codes';
  };

  return (
    <div className="app-container">
      <div className="scp-submit-code-page">
        <header className="scp-header">
          <h1 className="scp-title">코드 제출</h1>
        </header>

        <div className="scp-form-container">
          {/* 제목 입력 */}
          <div className="scp-form-group">
            <label htmlFor="title-input">제목</label>
            <input
              id="title-input"
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="scp-input"
              placeholder="제목을 입력하세요"
            />
          </div>

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
          <div className="scp-form-group">
            <label htmlFor="source-code">소스 코드</label>
            <CodeMirror
              value={sourceCode}
              height="600px"
              extensions={[languageExtensions[language]]}
              onChange={(value) => setSourceCode(value)}
              className="scp-code-input"
            />
          </div>

          {/* 버튼들 */}
          <div className="scp-submit-button-container">
            <button className="scp-submit-button" onClick={handleListClick}>
              목록
            </button>
            <button className="scp-submit-button" onClick={handleSubmit}>
              제출
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SubmitCodePage;
