import React, { useState } from 'react';
import './SubmitCodePage.css';

function SubmitCodePage() {
  // 상태 변수 정의
  const [language, setLanguage] = useState('Java 11'); // 선택된 프로그래밍 언어
  const [sourceCode, setSourceCode] = useState(''); // 소스 코드 입력
  const [title, setTitle] = useState(''); // 제목 상태 추가

  // 제출 핸들러
  const handleSubmit = async () => {
    console.log('제출된 제목:', title);
    console.log('제출된 소스 코드:', sourceCode);

    const token = localStorage.getItem('token'); // 로컬 저장소에서 JWT 토큰 가져오기
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const submissionData = {
      submittedCode: sourceCode,
      submissionDate: new Date().toISOString().split('T')[0], // 현재 날짜를 ISO 포맷으로 변환
    };

    try {
      const response = await fetch('http://localhost:8080/api/code/submit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`, // JWT 토큰 포함
        },
        body: JSON.stringify(submissionData),
      });

      if (!response.ok) {
        throw new Error('제출 실패'); // 오류 발생 시 예외 처리
      }

      // 성공 시 처리 로직
      window.location.href = '/grading'; // 제출 버튼 클릭 시 grading으로 이동
    } catch (error) {
      console.error('Error:', error);
      alert('제출 중 오류가 발생했습니다.');
    }
  };

  // 목록 페이지로 이동하는 핸들러
  const handleListClick = () => {
    const token = localStorage.getItem('token'); // 로컬 저장소에서 JWT 토큰 가져오기
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    // 목록 페이지로 이동
    window.location.href = '/submitted-codes'; // 목록 버튼 클릭 시 submitted-codes로 이동
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
              placeholder="제목을 입력하세요" // 플레이스홀더 추가
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
              placeholder="소스 코드를 입력하세요" // 플레이스홀더 추가
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
