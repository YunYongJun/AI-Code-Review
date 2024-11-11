import React, { useEffect } from 'react';
import AppRouter from './Router';
import MenuBar from './components/navigation/MenuBar';
import Footer from './components/navigation/Footer';
import './App.css';
//import AppRouter from './Router';
//import MenuBar from './components/MenuBar';

function App() {
  useEffect(() => {
    scheduleTokenRefresh();

    // 사용자 활동 감지
    window.addEventListener('mousemove', resetActivityTimer);
    window.addEventListener('keydown', resetActivityTimer);

    return () => {
      window.removeEventListener('mousemove', resetActivityTimer);
      window.removeEventListener('keydown', resetActivityTimer);
    };
  }, []);

  let refreshTimeout;

  function scheduleTokenRefresh() {
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const { exp } = JSON.parse(atob(token.split('.')[1])); // 토큰 만료 시간 가져오기
    const expirationTime = exp * 1000 - Date.now() - 60000; // 만료 1분 전

    clearTimeout(refreshTimeout);
    refreshTimeout = setTimeout(refreshToken, expirationTime);
  }

  function resetActivityTimer() {
    clearTimeout(refreshTimeout);
    scheduleTokenRefresh();
  }

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
