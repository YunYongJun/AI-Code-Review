import React, { useState } from 'react';
import './SubmittedCodes.css';

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
  const [editedDetail, setEditedDetail] = useState(''); // 수정된 내용 상태

  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    setEditedDetail(code.detail); // 선택된 코드의 detail로 초기화
  };

  const handleSave = () => {
    if (selectedCode) {
      // 수정된 내용을 적용
      const updatedCode = { ...selectedCode, detail: editedDetail };
      setSelectedCode(updatedCode);

      // 실제 데이터베이스 업데이트 로직이 필요할 경우 여기에 추가합니다.
      alert('코드가 저장되었습니다.');
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
            <textarea
              className="sc-input" // Add the input class for consistent styling
              value={editedDetail}
              onChange={(e) => setEditedDetail(e.target.value)}
              style={{ whiteSpace: 'pre-wrap', width: '100%', height: '200px' }}
            />
            <p>점수: {selectedCode.status}</p>
            <p>제출 시간: {selectedCode.submissionTime}</p>
            <button onClick={handleSave}>재채점</button>
          </>
        ) : (
          <p>코드를 선택해 주세요.</p>
        )}
      </div>
    </div>
  );
}

export default SubmittedCodes;
