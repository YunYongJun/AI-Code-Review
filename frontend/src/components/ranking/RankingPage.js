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
        setRankings(data);
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
  );
};

export default RankingPage;
