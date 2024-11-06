import React, { useState, useEffect } from 'react';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]);
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');

  // 페이지 로드 시 제출된 코드 목록을 가져옵니다.
  useEffect(() => {
    const fetchSubmittedCodes = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        alert('로그인이 필요합니다.');
        return;
      }

      try {
        const response = await fetch('http://localhost:8080/api/code/submitted', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setSubmittedCodes(data); // 제출된 코드 목록을 상태에 설정
        } else {
          throw new Error('제출된 코드 목록을 가져오는 데 실패했습니다.');
        }
      } catch (error) {
        console.error('Error:', error);
        alert('코드 목록을 가져오는 중 오류가 발생했습니다.');
      }
    };

    fetchSubmittedCodes();
  }, []); // 컴포넌트 마운트 시에만 실행됩니다.

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
      const response = await fetch('http://localhost:8080/api/code/resubmit', {
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

      // 성공적으로 제출된 후, 새로 제출된 코드를 목록에 추가
      const newCode = await response.json(); // 응답에서 새로 제출된 코드 데이터 받기
      setSubmittedCodes((prevCodes) => [newCode, ...prevCodes]); // 새로운 코드를 목록의 앞에 추가

      // 수정된 코드 선택을 해제
      setSelectedCode(null);
      setEditedDetail('');
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
