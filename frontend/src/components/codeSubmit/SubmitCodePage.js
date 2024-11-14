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
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.userId);
    }
  }, []);

  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  const handleSubmit = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    setIsLoading(true); // 로딩 시작
    const submissionData = {
      userId,
      code: sourceCode,
      title,
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

      alert('코드가 성공적으로 제출되었습니다.');
      window.location.href = '/submitted-codes';
    } catch (error) {
      console.error('Error:', error);
      alert('제출 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false); // 로딩 종료
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
      <div className={`scp-submit-code-page ${isLoading ? 'scp-blur' : ''}`}>
        <header className="scp-header">
          <h1 className="scp-title">코드 제출</h1>
        </header>

        <div className="scp-form-container">
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

          <div className="scp-form-group">
            <label htmlFor="source-code">소스 코드</label>
            <CodeMirror
              value={sourceCode}
              extensions={[languageExtensions[language]]}
              onChange={(value) => setSourceCode(value)}
              height="600px"
              className="scp-code-input"
            />
          </div>

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

      {isLoading && (
        <div className="scp-loading-overlay">
          <div className="scp-loading-spinner"></div>
        </div>
      )}
    </div>
  );
}

export default SubmitCodePage;
