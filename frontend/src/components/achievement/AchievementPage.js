import React, { useEffect, useState } from 'react';
import './AchievementPage.css';
import { jwtDecode } from 'jwt-decode';

const AchievementPage = () => {
  const [achievements, setAchievements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    // JWT 토큰에서 사용자 ID 추출
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode(token);
      setUserId(decodedToken.sub); // 사용자 ID 설정
    }
  }, []);

  useEffect(() => {
    // 사용자 ID가 있는 경우 업적을 가져옴
    const fetchAchievements = async () => {
      if (!userId) return; // 사용자 ID가 없으면 종료

      const token = localStorage.getItem('token');
      try {
        const response = await fetch(`http://localhost:8080/api/achievements/${userId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`, // JWT를 헤더에 포함
          },
        });

        if (!response.ok) {
          throw new Error('업적을 가져오는 데 실패했습니다.'); // 오류 처리
        }

        const data = await response.json();
        console.log('가져온 데이터:', data); // 가져온 데이터 확인
        setAchievements(data); // 업적 상태 설정
      } catch (error) {
        setError(error.message); // 오류 메시지 설정
      } finally {
        setLoading(false); // 로딩 상태 false로 설정
      }
    };

    fetchAchievements(); // 업적 가져오기 호출
  }, [userId]);

  if (loading) {
    return <div>로딩 중...</div>; // 로딩 상태
  }

  if (error) {
    return <div>오류: {error}</div>; // 오류 처리
  }

  return (
    <div className="app-container">
      <div className="achievement-page">
        <div className="achievement-description">
          <h3>사용자 업적 리스트</h3>
        </div>
        {/* 업적 테이블 */}
        <div className="achievement-table">
          <table>
            <thead>
              <tr>
                <th>업적 이름</th>
                <th>업적 설명</th>
                <th>달성 날짜</th>
              </tr>
            </thead>
            <tbody>
              {achievements.map((achievement) => (
                <tr key={achievement.id}>
                  <td>{achievement.achievementName}</td>
                  <td>{achievement.achievementDesc}</td>
                  <td>{achievement.dateAchieved}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AchievementPage;
