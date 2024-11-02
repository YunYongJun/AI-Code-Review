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
          .sort((a, b) => b.totalScore - a.totalScore) // Total Score로 정렬
          .map((item, index) => ({
            ...item,
            userRank: index + 1, // Rank 설정
            userId: String(item.userId), // userId를 문자열로 변환
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

  if (loading) {
    return <div>로딩 중...</div>;
  }

  if (error) {
    return <div>오류: {error}</div>;
  }

  return (
    <div className="app-container">
      <div className="ranking-page">
        {/* Ranking Table */}
        <div className="ranking-table">
          <table>
            <thead>
              <tr>
                <th>Rank</th>
                <th>User Id</th>
                <th>Total Score</th>
              </tr>
            </thead>
            <tbody>
              {rankings.map((ranking, index) => (
                <tr key={index}>
                  <td className={`ranking-${ranking.userRank}`}>{ranking.userRank}</td>
                  <td>{ranking.userId}</td>
                  <td>{ranking.totalScore}</td>
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
