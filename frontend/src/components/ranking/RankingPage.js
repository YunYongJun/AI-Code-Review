// src/RankingPage.js
import React from 'react';
import './RankingPage.css';

const RankingPage = () => {
  return (
    <div className="ranking-page">
      <div className="ranking-user-profile">
        <div className="ranking-user-avatar">👤</div>
        <div className="ranking-user-info">
          <h2>윤용준</h2>
          <div className="ranking-user-rank">
            Silver 13
            <div className="ranking-progress-bar">
              <div className="ranking-progress" style={{ width: '70%' }}></div>
            </div>
            Gold 승급까지 -17
          </div>
          <div className="ranking-user-achievements">13개의 업적</div>
        </div>
        <button className="ranking-page-menu-icon">☰</button>
      </div>

      {/* Ranking Table */}
      <div className="ranking-table">
        <table>
          <thead>
            <tr>
              <th>User Id</th>
              <th>Tier</th>
              <th>획득한 업적 수</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>박승아</td>
              <td className="ranking-gold">Gold</td>
              <td>56</td>
            </tr>
            <tr>
              <td>윤용준</td>
              <td className="ranking-silver">Silver</td>
              <td>13</td>
            </tr>
            <tr>
              <td>박기량</td>
              <td className="ranking-bronze">Bronze</td>
              <td>8</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default RankingPage;
