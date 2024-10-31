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
    // 코드 제출 페이지는 로그인 여부에 관계없이 이동 가능
    if (url === '/submission' || isLoggedIn) {
      window.location.href = url;
    } else {
      alert('로그인이 필요합니다.');
    }
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
        <span onClick={() => handleMenuClick('/ranking')} className="menu-item">
          <FaChartLine className="menu-icon" />
          순위
        </span>

        <span onClick={() => handleMenuClick('/achievement')} className="menu-item">
          <FaTrophy className="menu-icon" />
          업적
        </span>

        <span onClick={() => handleMenuClick('/submission')} className="menu-item">
          <FaStar className="menu-icon" />
          코드 제출
        </span>
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
