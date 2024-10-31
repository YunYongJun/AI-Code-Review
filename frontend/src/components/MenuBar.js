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
    window.location.href = '/login';
  };

  return (
    <div className="menu-bar">
      <div className="menu-logo">
        <a href="/" className="logo-link">
          CODE<span className="logo-highlight">REVIEW</span>
        </a>
        <span className="logo-subtext">코드 체점 사이트</span>
      </div>

      <div className="menu-items">
        <a href="/ranking" className="menu-item">
          <FaChartLine className="menu-icon" />
          순위
        </a>

        <a href="/achievement" className="menu-item">
          <FaTrophy className="menu-icon" />
          업적
        </a>

        <a href="/submission" className="menu-item">
          <FaStar className="menu-icon" />
          코드 제출
        </a>
      </div>

      <div className="menu-auth">
        {isLoggedIn ? (
          <>
            <a href="/profile-edit" className="menu-item">
              안녕하세요, {username}님!
            </a>
            <button onClick={handleLogout} className="menu-item logout-button" style={{ cursor: 'pointer' }}>
              <FaSignOutAlt className="menu-icon" />
              로그아웃
            </button>
          </>
        ) : (
          <>
            <a href="/login" className="menu-item">
              로그인
            </a>
            <a href="/signup" className="menu-item">
              회원가입
            </a>
          </>
        )}
      </div>
    </div>
  );
};

export default MenuBar;
