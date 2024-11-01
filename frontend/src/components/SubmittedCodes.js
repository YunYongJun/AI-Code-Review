import React, { useState } from 'react';
import './SubmittedCodes.css'; // 스타일링 파일

// 임의의 제출된 코드 데이터
const submittedCodes = [
  {
    id: 1,
    title: '테스트',
    status: '0',
    submissionTime: '2024-10-21 10:30',
    detail: 'console.log("Hello World") // 세미콜론이 없습니다. \nint n = true; // 형식이 맞지 않습니다. \n\n\n\n',
  },
  {
    id: 2,
    title: '회원가입',
    status: '2.3',
    submissionTime: '2024-10-21 11:00',
    detail: 'function nextNum(n) { return n + 1; }\n\n\n\n',
  },
  {
    id: 3,
    title: '로그인',
    status: '2.2',
    submissionTime: '2024-10-21 11:30',
    detail: 'function sumOfConsecutiveNumbers(arr) { return arr.reduce((a, b) => a + b); }\n\n\n\n',
  },
  {
    id: 4,
    title: '게시판 기능 구현',
    status: '1.0',
    submissionTime: '2024-10-21 12:00',
    detail: 'function cutPaper(m, n) { return m * n - 1; }\n\n\n\n',
  },
];

function SubmittedCodes() {
  const [selectedCode, setSelectedCode] = useState(null);

  return (
    <div className="submitted-codes-page">
      <div className="code-list">
        <h3>제출 코드 목록</h3>
        <ul>
          {submittedCodes.map((code) => (
            <li key={code.id} onClick={() => setSelectedCode(code)}>
              {code.title}
            </li>
          ))}
        </ul>
      </div>
      <div className="code-details">
        {selectedCode ? (
          <>
            <h4>{selectedCode.title}</h4>
            <p style={{ whiteSpace: 'pre-wrap' }}>{selectedCode.detail}</p> {/* 줄바꿈을 유지하는 스타일 적용 */}
            <p>점수: {selectedCode.status}</p>
            <p>제출 시간: {selectedCode.submissionTime}</p>
            <a href="/submission">
              <button>수정</button>
            </a>
          </>
        ) : (
          <p>코드를 선택해 주세요.</p>
        )}
      </div>
    </div>
  );
}

export default SubmittedCodes;
