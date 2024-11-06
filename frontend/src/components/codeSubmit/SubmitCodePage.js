import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import './SubmitCodePage.css';

function SubmitCodePage() {
  const [language, setLanguage] = useState('Java 11'); // 선택된 프로그래밍 언어
  const [sourceCode, setSourceCode] = useState(''); // 소스 코드 입력
  const [title, setTitle] = useState(''); // 제목 상태 추가
  const [userId, setUserId] = useState(''); // 사용자 ID 상태 추가

  // JWT 토큰에서 사용자 ID 추출
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.userId); // 사용자 ID 설정
    }
  }, []);

  // 제출 핸들러
  const handleSubmit = async () => {
    console.log('제출된 제목:', title);
    console.log('제출된 소스 코드:', sourceCode);
    console.log('제출된 사용자 ID:', userId);

    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const submissionData = {
      prompt: sourceCode, // 'prompt' 필드로 수정
      submissionDate: new Date().toISOString().split('T')[0], // 현재 날짜
      //userId: userId, // 사용자 ID 추가
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
              <option value="Java 11">Java 11</option>
              <option value="Python 3">Python 3</option>
              <option value="C++">C++</option>
            </select>
          </div>

          {/* 소스 코드 입력 */}
          <div className="scp-form-group">
            <label htmlFor="source-code">소스 코드</label>
            <textarea
              id="source-code"
              value={sourceCode}
              onChange={(e) => setSourceCode(e.target.value)}
              className="scp-code-input"
              rows="10"
              placeholder="소스 코드를 입력하세요"
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
