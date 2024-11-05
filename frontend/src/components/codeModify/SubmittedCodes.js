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
  // 다른 코드들...
];

function SubmittedCodes() {
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');

  // 코드 선택 핸들러
  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    setEditedDetail(code.detail);
  };

  // 수정된 코드 제출 핸들러
  const resubmitCode = async () => {
    if (!selectedCode) {
      alert('수정할 코드를 선택해 주세요.');
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const submissionData = {
      revisedCode: editedDetail, // 수정된 코드 내용
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

      if (!response.ok) {
        throw new Error('수정된 코드 제출 실패');
      }

      alert('수정된 코드가 제출되었습니다.');
      // 필요시 추가적인 후속 처리 가능
    } catch (error) {
      console.error('Error:', error);
      alert('코드 제출 중 오류가 발생했습니다.');
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
              className="sc-input"
              value={editedDetail}
              onChange={(e) => setEditedDetail(e.target.value)}
              style={{ whiteSpace: 'pre-wrap', width: '100%', height: '200px' }}
            />
            <p>점수: {selectedCode.status}</p>
            <p>제출 시간: {selectedCode.submissionTime}</p>
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
