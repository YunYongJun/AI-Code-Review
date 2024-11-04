// src/AchievementPage.js
import React from 'react';
import './AchievementPage.css';

const AchievementPage = () => {
  return (
    <div className="app-container">
      <div className="achievement-page">
        <div className="achievement-user-profile"> {/* Updated class name */}
          <div className="achievement-user-avatar">👤</div>
          <div className="achievement-user-info">
            <h2>사용자ID...1</h2>
            <div className="achievement-user-rank">
              Silver V 295
              <div className="achievement-progress-bar">
                <div className="achievement-progress" style={{ width: '70%' }}></div>
              </div>
              Silver IV 승급까지 -5
            </div>
            <div className="achievement-user-achievements">13개의 업적</div>
          </div>
          <button className="achievement-menu-icon">☰</button>
        </div>

        {/* Achievements List */}
        <div className="achievement-list"> {/* Updated class name */}
          <div className="achievement-item">
            <h3>신입 문제해결사</h3>
            <p>10문제 해결</p>
          </div>
          <div className="achievement-item">
            <h3>새싹3단계</h3>
            <p>8일 연속 문제 해결</p>
          </div>
          <div className="achievement-item">
            <h3>새싹2단계</h3>
            <p>4일 연속 문제 해결</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AchievementPage;
