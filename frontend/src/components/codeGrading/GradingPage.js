import React, { useEffect, useState } from 'react';
import './GradingPage.css';

function GradingPage() {
  const [loading, setLoading] = useState(true); // 로딩 상태 관리
  const [tipIndex, setTipIndex] = useState(0);  // 표시할 팁의 인덱스 관리
  const [dots, setDots] = useState("");         // 점 애니메이션 상태 관리

  // 로딩 중에 표시될 팁 목록
  const tips = [
    "Tip 1: 상단 카테고리바에 있는 순위 버튼을 클릭하면 누적 점수에 따른 전체 순위를 확인할 수 있습니다.",
    "Tip 2: 상단 로그아웃 버튼 왼쪽에 있는 사용자 이름을 클릭하면 개인 정보를 수정할 수 있습니다.",
    "Tip 3: CODEREVIEW 로고를 클릭하면 메인화면으로 이동합니다.",
    "Tip 4: 상단 카테고리바에 있는 코드 제출 버튼을 클릭한 후, 코드를 입력하여 체점을 받을 수 있습니다.",
    "Tip 5: 로그인을 하지 않으면, 업적을 볼 수 없고, 체점 기능을 사용할 수 없습니다."
  ];

  useEffect(() => {
    // 로딩 상태가 지속될 때마다 점이 추가되는 타이머 설정
    const dotsInterval = setInterval(() => {
      setDots((prevDots) => (prevDots.length < 5 ? prevDots + "." : ""));
    }, 700);

    // 로딩이 완료되는 타이머 설정 (로딩 완료되고 페이지 이동)
    const loadingTimer = setTimeout(() => {
      setLoading(false);
      window.location.href = '/submitted-codes';
    }, 10000);

    // 팁 인덱스를 10초 간격으로 업데이트하여 다른 팁을 표시
    const tipTimer = setInterval(() => {
      setTipIndex((prevIndex) => (prevIndex + 1) % tips.length);
    }, 10000); // 10초 간격

    // 컴포넌트가 언마운트될 때 타이머와 인터벌을 정리하여 메모리 누수 방지 
    return () => {
      clearTimeout(loadingTimer);   // 로딩 타이머 정리
      clearInterval(dotsInterval);  // 점 애니메이션 인터벌 정리
      clearInterval(tipTimer);      // 팁 변경 인터벌 정리
    };
  }, [tips.length]);

  return (
    <div className="gpg-grading-page">
      <header className="gpg-header">
        <h1 className="gpg-title">채점 중입니다{dots}</h1>
      </header>

      <div className="gpg-form-container">
        {loading ? (
          <>
            <div className="gpg-loader"></div>
            <div className="gpg-tip-section">
              <p className="gpg-tip">{tips[tipIndex]}</p>
            </div>
          </>
        ) : (
          <div className="gpg-completion-message">
            <p>채점이 완료되었습니다.</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default GradingPage;
