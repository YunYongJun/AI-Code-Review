import React, { useEffect, useState } from 'react';
import './RankingPage.css';

const RankingPage = () => {
  const [rankings, setRankings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRankings = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/rankings');
       
        if (!response.ok) {
          throw new Error('네트워크 응답이 좋지 않습니다.');
        }

        const data = await response.json();
        // Total Score 기준으로 내림차순 정렬 후 Rank 설정
        const sortedData = data
          .sort((a, b) => b.totalScore - a.totalScore)
          .map((item, index) => ({
            ...item,
            userRank: index + 1,
            userId: String(item.userId),
          }));

        setRankings(sortedData);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRankings();
  }, []);

  // 점수에 따른 등급을 반환하는 함수
  const getRankLabel = (score) => {
    if (score > 2000) {
      return 'gold'; // 클래스 이름
    } else if (score > 1000) {
      return 'silver'; // 클래스 이름
    } else if (score > 500) {
      return 'bronze'; // 클래스 이름
    }
    return 'newbie'; // 클래스 이름
  };

  if (loading) {
    return <div>로딩 중...</div>;
  }

  if (error) {
    return <div>오류: {error}</div>;
  }

  return (
    <div className="app-container">
      <div className="ranking-page">
        {/* 설명문 추가 */}
        <div className="ranking-description">
          <h3>점수에 따른 등급</h3>
          <ul>
            <li>0: <span className="ranking-newbie">Newbie </span>
              | 500: <span className="ranking-bronze">Bronze </span>
              | 1000: <span className="ranking-silver">Silver </span>
              | 2000: <span className="ranking-gold">Gold</span></li>
          </ul>
        </div>
        {/* Ranking Table */}
        <div className="ranking-table">
          <table>
            <thead>
              <tr>
                <th>Rank</th>
                <th>User Id</th>
                <th>Total Score</th>
                <th>등급</th> {/* 등급 열 추가 */}
              </tr>
            </thead>
            <tbody>
              {rankings.map((ranking, index) => (
                <tr key={index}>
                  <td>{ranking.userRank}</td>
                  <td>{ranking.user.username}</td>
                  <td>{ranking.totalScore}</td>
                  <td className={getRankLabel(ranking.totalScore)}>{getRankLabel(ranking.totalScore)}</td> {/* 등급 표시 */}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default RankingPage;
