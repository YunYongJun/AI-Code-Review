import React from 'react';
import './MainPage.css';

function MainPage() {
  return (
    <div className="mainPage-App">
      <header className="mainPage-header">
        <div className="mainPage-header-content">
          <div className="mainPage-spring-label">대학 기업 협력형 SW아카데미</div>
          <h1>IoT 빅데이터 응용 교육과정<br /> 교육생 모집</h1>
          <button className="mainPage-signup-button">선착순 마감</button>
          <p>IoT 7기 지원하여 취업하기</p>
        </div>
      </header>

      <section className="mainPage-code-review-section">
        <h2>Code Review</h2>
        <div className="mainPage-underline"></div>
        <p>작성한 코드를 분석, 수정, 체점받을 수 있는 곳입니다.</p>
      </section>
    </div>
  );
}

export default MainPage;
