import React, { useState } from 'react';
import './SubmitCodePage.css';

function SubmitCodePage() {
  const [language, setLanguage] = useState('Java 11');
  const [sourceCode, setSourceCode] = useState('');
  const [title, setTitle] = useState(''); // 제목 상태 추가

  const handleSubmit = () => {
    // 제출 처리 로직
    console.log('제출된 제목:', title);
    console.log('제출된 소스 코드:', sourceCode);
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
          <a href="/submitted-codes" className="menu-item">
            <button className="scp-submit-button">목록</button>
          </a>
          <a href="/grading" className="menu-item">
            <button className="scp-submit-button" onClick={handleSubmit}>
              제출
            </button>
          </a>
        </div>
      </div>
    </div>
  );
}

export default SubmitCodePage;
