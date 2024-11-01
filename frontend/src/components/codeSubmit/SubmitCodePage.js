import React, { useState } from 'react';
import './SubmitCodePage.css';

function SubmitCodePage() {
  const [language, setLanguage] = useState('Java 11');
  const [sourceCode, setSourceCode] = useState('');
  const [title, setTitle] = useState(''); // 제목 상태 추가

  const handleSubmit = async () => {
    // 제출 처리 로직
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

      // 성공 시 처리 로직 (예: 제출 완료 메시지, 리디렉션 등)
      window.location.href = '/grading'; // 제출 버튼 클릭 시 grading으로 이동
    } catch (error) {
      console.error('Error:', error);
      alert('제출 중 오류가 발생했습니다.');
    }
  };

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
    <div className="scp-submit-code-page">
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
            <option value="Java 11">Java 11</option>
            <option value="Python 3">Python 3</option>
            <option value="C++">C++</option>
          </select>
        </div>


        <div className="scp-form-group">
          <label htmlFor="source-code">소스 코드</label>
          <textarea
            id="source-code"
            value={sourceCode}
            onChange={(e) => setSourceCode(e.target.value)}
            className="scp-code-input"
            rows="10"
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
  );
}

export default SubmitCodePage;
