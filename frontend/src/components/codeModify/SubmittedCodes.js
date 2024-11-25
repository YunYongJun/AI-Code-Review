import React, { useState, useEffect } from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';
import { cpp } from '@codemirror/lang-cpp';
import { jwtDecode } from 'jwt-decode';
import './SubmittedCodes.css';

function SubmittedCodes() {
  const [submittedCodes, setSubmittedCodes] = useState([]);
  const [selectedCode, setSelectedCode] = useState(null);
  const [editedDetail, setEditedDetail] = useState('');
  const [language, setLanguage] = useState('java');

  const languageExtensions = {
    java: java(),
    python: python(),
    cpp: cpp(),
  };

  // 제출된 코드 목록 로드
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      const userIdFromToken = decodedToken.userId;

      if (userIdFromToken) {
        fetch(`http://localhost:8080/api/code/submissions?userId=${userIdFromToken}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        })
          .then((response) => {
            if (!response.ok) throw new Error('코드 제출 목록을 불러오는 데 실패했습니다.');
            return response.json();
          })
          .then((data) => setSubmittedCodes(data))
          .catch((error) => console.error('Error:', error));
      } else {
        console.error('JWT 토큰에서 userId를 찾을 수 없습니다.');
      }
    } else {
      console.error('로컬 스토리지에 토큰이 없습니다.');
    }
  }, []);

  // 코드 선택 처리
  const handleCodeSelect = (code) => {
    setSelectedCode(code);
    setEditedDetail(code.revisedCode || code.initialCode);
    setLanguage(code.language || 'java');
  };

  // 코드 수정
  const resubmitCode = async () => {
    if (!selectedCode) {
      alert('수정할 코드를 선택해 주세요.');
      return;
    }

    // 수정된 코드 제출에 필요한 데이터 준비
    const resubmissionData = new URLSearchParams();
    resubmissionData.append('submissionId', selectedCode.id);
    resubmissionData.append('revisedCode', editedDetail);

    // 로컬 스토리지에서 JWT 토큰 가져오기
    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    try {
      // API 요청: Authorization 헤더에 JWT 토큰 추가
      const response = await fetch('http://localhost:8080/api/code/revise', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': `Bearer ${token}`, // Authorization 헤더 추가
        },
        body: resubmissionData.toString(),
      });

      if (!response.ok) {
        throw new Error('수정된 코드 제출 실패');
      }

      // 수정된 코드 응답 받기
      const updatedCode = await response.json();
      setSelectedCode(updatedCode); // 수정된 코드 상태 업데이트

      alert('수정된 코드가 제출되었습니다.');
    } catch (error) {
      console.error('Error:', error);
      alert('코드 제출 중 오류가 발생했습니다.');
    }
  };


  // AI 피드백 포맷팅
  const formatFeedback = (feedback) => {
    if (!feedback) return '아직 피드백이 없습니다.';
    return feedback.replace(/###결론###/, '\n###결론###');
  };

  // Pylint 결과 포맷팅
  const formatPylintOutput = (output) => {
    if (!output) return 'Pylint 결과가 없습니다.';

    // 불필요한 라인 제거: DeprecationWarning 및 ************* Module 관련 라인
    const cleanedOutput = output
      .split('\n')
      .filter(
        (line) =>
          !line.includes('DeprecationWarning') &&
          !line.startsWith('************* Module') &&
          !line.includes('(pylint_stdout, _) = lint.py_run(file_path, return_std=True)')
      );

    // 파일 경로를 'py:{line}' 형태로 변경
    const formattedOutput = cleanedOutput
      .map((line) => line.replace(/.*\\Temp\\[^\\]+\.py:(\d+):/, 'py:$1:'))
      .join('\n');

    return formattedOutput.trim();
  };

  return (
    <div className="sc-submitted-codes-page">
      {/* 제출 코드 목록 */}
      <div className="sc-code-list">
        <h3>제출 코드 목록</h3>
        <ul>
          {submittedCodes.map((code, index) => (
            <li key={index} onClick={() => handleCodeSelect(code)}>
              {code.title || `제출 코드 ${index + 1}`}
            </li>
          ))}
        </ul>
      </div>

      {/* 선택된 코드 세부 정보 */}
      <div className="sc-code-details">
        {selectedCode ? (
          <>
            <h4>{selectedCode.title}</h4>
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

            <CodeMirror
              value={editedDetail}
              extensions={[languageExtensions[language] || java()]}
              onChange={(value) => setEditedDetail(value)}
              height="400px"
            />

            <div className="sc-feedback-section">
              <h5>AI 피드백</h5>
              <pre>
                {selectedCode.revisedPylintOutput
                  ? formatPylintOutput(selectedCode.revisedPylintOutput)
                  : formatPylintOutput(selectedCode.pylintOutput)}
              </pre>
              {/* <pre>{formatFeedback(selectedCode.feedback)}</pre> */}
            </div>



            <p>초기 점수: {selectedCode.initialScore}</p>
            <p>수정 후 점수: {selectedCode.revisedScore}</p>

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
