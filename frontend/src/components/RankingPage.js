// src/RankingPage.js
import React from 'react';
import './RankingPage.css';

const RankingPage = () => {
  return (
    <div className="ranking-page">
      <div className="user-profile">
        <div className="user-avatar">👤</div>
        <div className="user-info">
          <h2>윤용준</h2>
          <div className="user-rank">
            Silver 13
            <div className="progress-bar">
              <div className="progress" style={{ width: '70%' }}></div>
            </div>
            Gold 승급까지 -17
          </div>
          <div className="user-achievements">13개의 업적</div>
        </div>
        <button className="menu-icon">☰</button>
      </div>

      {/* Ranking Table */}
      <div className="ranking-table">
        <table>
          <thead>
            <tr>
              <th>User Id</th>
              <th>tier</th>
              <th>획득한 업적 수</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>박승아</td>
              <td className="gold">Gold</td>
              <td>56</td>
            </tr>
            <tr>
              <td>윤용준</td>
              <td className="silver">silver</td>
              <td>13</td>
            </tr>
            <tr>
              <td>박기량</td>
              <td className="bronze">bronze</td>
              <td>8</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default RankingPage;
