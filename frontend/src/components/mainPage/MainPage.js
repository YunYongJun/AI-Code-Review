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
        <div className="underline"></div>
        <p>작성한 코드를 분석, 수정, 체점받을 수 있는 곳입니다.</p>
      </section>

      {/* 여기에 새로운 섹션을 추가합니다 */}
      <section className="mainPage-multi-column-section">
        <div className="mainPage-multi-column-container">
          <div className="mainPage-column">
            <h3>Daimond</h3>
            <ul>
              <li>이현우 2020243052</li>
              <li>박성호 2010243010</li>
              <li>박승아 2012243112</li>
              <li>박기량 2024243011</li>
              <li>윤용준 2025244122</li>
            </ul>
          </div>
          <div className="mainPage-column">
            <h3>역할</h3>
            <ul>
              <li>Front</li>
              <li>Back</li>
              <li>Front</li>
              <li>AI</li>
              <li>Back</li>
            </ul>
          </div>
          <div className="mainPage-column">
            <h3>직책</h3>
            <ul>
              <li>스크럼 마스터</li>
            </ul>
          </div>
        </div>
      </section>

      <footer className="mainPage-footer">
        <div className="mainPage-footer-content">
          <p>&copy; 2024 All Rights Reserved. CODEREVIEW</p>
        </div>
      </footer>
    </div>
  );
}

export default MainPage;
