import React, { useEffect } from 'react';
import AppRouter from './Router';
import MenuBar from './components/navigation/MenuBar';
import Footer from './components/navigation/Footer';
import './App.css';

function App() {
  // 컴포넌트가 처음 렌더링될 때 토큰 갱신 스케줄링 및 사용자 활동 감지 이벤트 설정
  useEffect(() => {
    // 토큰 만료 전 갱신 스케줄링
    scheduleTokenRefresh();

    // 사용자 활동 감지: 마우스 이동 또는 키보드 입력 시 활동 타이머 리셋
    window.addEventListener('mousemove', resetActivityTimer);
    window.addEventListener('keydown', resetActivityTimer);

    // 컴포넌트가 언마운트될 때 이벤트 리스너 정리
    return () => {
      window.removeEventListener('mousemove', resetActivityTimer);
      window.removeEventListener('keydown', resetActivityTimer);
    };
  }, []); // 빈 배열로 최초 렌더링 후 한 번만 실행

  // 토큰 갱신을 위한 타이머 변수
  let refreshTimeout;

  // 토큰 갱신 스케줄링 함수
  function scheduleTokenRefresh() {
    // 로컬스토리지에서 JWT 토큰 가져오기
    const token = localStorage.getItem('jwt');

    // 토큰이 없으면 함수 종료
    if (!token) return;

    const { exp } = JSON.parse(atob(token.split('.')[1]));  // 토큰 만료 시간 가져오기
    const expirationTime = exp * 1000 - Date.now() - 60000; // 만료 1분 전

    // 이전에 설정된 타이머를 클리어
    clearTimeout(refreshTimeout);

    // 새로운 갱신 타이머 설정
    refreshTimeout = setTimeout(refreshToken, expirationTime);
  }

  // 사용자 활동이 있을 때마다 타이머 리셋
  function resetActivityTimer() {
    clearTimeout(refreshTimeout);
    scheduleTokenRefresh();
  }

  // 토큰 갱신 함수
  function refreshToken() {
    fetch('/api/auth/refresh', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    })
      .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Token expired');
      })
      .then(data => {
        localStorage.setItem('jwt', data.token);
        scheduleTokenRefresh();
      })
      .catch(() => {
        localStorage.removeItem('jwt');
        alert("세션이 만료되었습니다. 다시 로그인해주세요.");
        window.location.href = '/login';
      });
  }

  return (
    <>
      <MenuBar />
      <AppRouter />
      <Footer />
    </>
  );
}

export default App;
