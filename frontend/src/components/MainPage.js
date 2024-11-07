import React from 'react';
import './MainPage.css';

function MainPage() {
  return (
    <div className="App">
      <header className="header">
        <div className="header-content">
          <div className="spring-label">대학 기업 협력형 SW아카데미</div>
          <h1>IoT 빅데이터 응용 교육과정<br/> 교육생 모집</h1>
          <button className="signup-button">선착순 마감</button>
          <p>IoT 7기 지원하여 취업하기</p>
        </div>
      </header>

      <section className="code-review-section">
        <h2>Code Review</h2>
      </section>

      <footer className="footer">
        <div className="footer-content">
          <p>&copy; 2024 All Rights Reserved. CODEREVIEW</p>
        </div>
      </footer>
    </div>
  );
}

export default MainPage;