import React, { useState, useEffect } from 'react';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]); // 제출 코드 목록 상태
  const [selectedCode, setSelectedCode] = useState(null); // 선택된 코드의 상세 정보 상태
  const [editedDetail, setEditedDetail] = useState(''); // 수정된 코드 내용
  const [language, setLanguage] = useState('java'); // 언어 상태

  const token = localStorage.getItem('token');

  // 코드 목록 가져오기
  useEffect(() => {
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const fetchSubmittedCodes = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/code/submissions', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error('코드 목록 가져오기 실패');
        }

        const data = await response.json();
        setSubmittedCodes(data); // 가져온 코드 목록 설정
      } catch (error) {
        console.error('Error:', error);
        alert('코드 목록을 가져오는 중 오류가 발생했습니다.');
      }
    };

    fetchSubmittedCodes();
  }, [token]);

  // 코드 선택 핸들러
  const handleCodeSelect = async (code) => {
    if (!code || !code.id) {
      console.error("Code 객체에 id가 없습니다:", code);
      return;
    }

    setSelectedCode(code); // 선택한 코드 설정
    setEditedDetail(code.detail); // 선택한 코드의 상세 내용을 수정 필드에 설정
    setLanguage(code.language || 'java'); // 선택된 코드의 언어로 초기화

    try {
      const response = await fetch(`http://localhost:8080/api/code/submissions/${code.id}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('코드 세부 정보 가져오기 실패');
      }

      const detailData = await response.json();
      setSelectedCode(detailData);
      setEditedDetail(detailData.detail);
      setLanguage(detailData.language || 'java');
    } catch (error) {
      console.error('Error:', error);
      alert('코드 세부 정보를 가져오는 중 오류가 발생했습니다.');
    }
  };

  // 수정된 코드 제출 핸들러
  const resubmitCode = async () => {
    if (!selectedCode) {
      alert('수정할 코드를 선택해 주세요.');
      return;
    }

    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const submissionData = {
      revisedCode: editedDetail, // 수정된 코드 내용
      language, // 언어 정보
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

      alert('수정된 코드가 성공적으로 제출되었습니다.');
    } catch (error) {
      console.error('Error:', error);
      alert('수정된 코드 제출 중 오류가 발생했습니다.');
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

            {/* 언어 선택 */}
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

            {/* 수정 가능한 텍스트 영역 */}
            <textarea
              value={editedDetail}
              onChange={(e) => setEditedDetail(e.target.value)}
              rows="10"
              className="sc-code-input"
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
