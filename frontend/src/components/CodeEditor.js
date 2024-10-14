import React from 'react';
import './CodeEditor.css';

function CodeEditor() {
  const errors = [
    { id: 1, title: 'int Helloworld;', message: '변수명이 필요했습니다.', time: '10 min' },
    { id: 2, title: 'int num;', message: '변수 초기화를 하지 않았습니다.', time: '10 min' },
    { id: 3, title: 'char name;', message: '변수명이 필요했습니다.', time: '20 min' },
    { id: 4, title: 'char name2;', message: '변수 초기화를 하지 않았습니다.', time: '20 min' }
  ];

  const handleSubmit = () => {
    console.log("제출하기 clicked");
    // Add submit functionality here
  };

  const handleGrade = () => {
    console.log("체점하기 clicked");
    // Add grading functionality here
  };

  return (
    <div className="app-container">
      {/* Sidebar */}
      <div className="sidebar">
        <h2>코드 체점기</h2>
        <nav>
          <ul>
            <li>
              <button className="sidebar-btn" onClick={handleSubmit}>
                제출하기
              </button>
            </li>
            <li>
              <button className="sidebar-btn" onClick={handleGrade}>
                체점하기
              </button>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main Content */}
      <div className="main-content">
        {/* Error List */}
        <div className="error-list">
          <h3>잘못된 코드 목록</h3>
          <ul>
            {errors.map(error => (
              <li key={error.id}>
                <strong>{error.title}</strong>
                <p>{error.message}</p>
                <span>{error.time}</span>
              </li>
            ))}
          </ul>
        </div>

        {/* Code Editor */}
        <div className="code-editor">
          <h3>문제 수정</h3>
          <pre>
            <code>
              {`def add(x):\n    return x+5\n\ndef dowrite(ast):\n    nodename = getNodename()\n    # Some other code here...\n`}
            </code>
          </pre>
        </div>
      </div>
    </div>
  );
}

export default CodeEditor;
