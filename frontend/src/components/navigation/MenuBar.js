// src/MenuBar.js
import React, { useState, useEffect } from 'react';
import './MenuBar.css';
import { FaChartLine, FaTrophy, FaStar, FaSignOutAlt } from 'react-icons/fa';
import { jwtDecode } from 'jwt-decode'; // 명명된 import로 수정

const MenuBar = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      setIsLoggedIn(true);
      const decodedToken = jwtDecode(token);
      setUsername(decodedToken.sub); // 토큰에서 사용자 이름 추출
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsLoggedIn(false);
    setUsername('');
    alert('로그아웃 되었습니다.');
    window.location.href = '/';
  };

  const handleMenuClick = (url) => {
    // 코드 제출 페이지와 랭킹 페이지는 로그인 여부에 관계없이 이동 가능
    if (url === '/submission' || url === '/ranking' || isLoggedIn) {
      window.location.href = url;
    } else {
      alert('로그인이 필요합니다.');
    }
  };

  return (
    <div className="menuBar-bar">
      <div className="menuBar-logo">
        <a href="/" className="menuBar-logo-link">
          CODE<span className="menuBar-logo-highlight">REVIEW</span>
        </a>
        <span className="menuBar-logo-subtext">코드 체점 사이트</span>
      </div>

      <div className="menuBar-items">
        <span onClick={() => handleMenuClick('/ranking')} className="menuBar-item">
          <FaChartLine className="menuBar-icon" />
          순위
        </span>

        <span onClick={() => handleMenuClick('/achievement')} className="menuBar-item">
          <FaTrophy className="menuBar-icon" />
          업적
        </span>

        <span onClick={() => handleMenuClick('/submission')} className="menuBar-item">
          <FaStar className="menuBar-icon" />
          코드 제출
        </span>
      </div>

      <div className="menuBar-auth">
        {isLoggedIn ? (
          <>
            <a href="/profile-edit" className="menuBar-item">
              안녕하세요, {username}님!
            </a>
            <button onClick={handleLogout} className="menuBar-item menuBar-logout-button">
              <FaSignOutAlt className="menuBar-icon" />
              로그아웃
            </button>
          </>
        ) : (
          <>
            <a href="/login" className="menuBar-item">
              로그인
            </a>
            <a href="/signup" className="menuBar-item">
              회원가입
            </a>
          </>
        )}
      </div>
    </div>
  );
};

export default MenuBar;
