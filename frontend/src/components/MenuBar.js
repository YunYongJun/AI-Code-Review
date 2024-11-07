import React from 'react';
import './MenuBar.css';  // CSS 파일 import
import { FaChartLine, FaTrophy, FaStar, FaSignInAlt, FaUserPlus } from 'react-icons/fa';  // 아이콘 import

const MenuBar = () => {
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

        <a href="/submissions" className="menu-item">
          <FaStar className="menu-icon" />
          코드 제출
        </a>
      </div>

      <div className="menu-auth">
        <a href="/login" className="menu-item">
          <FaSignInAlt className="menu-icon" />
          로그인
        </a>

        <a href="/signup" className="menu-item">
          <FaUserPlus className="menu-icon" />
          회원가입
        </a>
      </div>
    </div>
  );
};

export default MenuBar;
