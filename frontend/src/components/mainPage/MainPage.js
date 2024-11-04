import React, { useEffect, useState } from 'react';
import './MainPage.css';
import { FaSearch, FaPencilAlt, FaCheckCircle } from 'react-icons/fa';

const images = [
  "/background1.jpg",
  "/background2.jpg",
  "/background3.jpg",
  "/background4.jpg",
  "/background5.jpg"
];

function MainPage() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 10000); // 10초 간격

    return () => clearInterval(interval); // 컴포넌트가 언마운트될 때 interval 클리어
  }, []);

  return (
    <div className="mainPage-App" style={{ backgroundImage: `url(${images[currentImageIndex]})` }}>
      <header className="mainPage-header">
        <div className="mainPage-header-content">
          <div className="mainPage-spring-label">대학 기업 협력형 SW아카데미</div>
          <h1>IoT 빅데이터 응용 교육과정<br /> 교육생 모집</h1>
          <h2>CODE<span className="review">REVIEW</span></h2>
          <p>작성한 코드를 분석, 수정, 체점받을 수 있는 곳입니다.</p>
        </div>
      </header>

      {/* 분석, 수정, 체점 박스 섹션 */}
      <div className="box-container">

        <div className="box">
          <img src="/analyze.png" className="box-icon1" />
          <div className="box-title">분석</div>
          <div className="box-subtitle">코드제출을 통해 코드를 정확하게 분석합니다</div>
        </div>

        <div className="box">
          <img src="/edit.png" className="box-icon2" />
          <div className="box-title">수정</div>
          <div className="box-subtitle">제출 코드 목록에서 본인의 코드를 수정할 수 있습니다</div>
        </div>

        <div className="box">
          <img src="/score.png" className="box-icon3" />
          <div className="box-title">체점</div>
          <div className="box-subtitle">코드를 제출하면 해당 코드를 체점합니다</div>
        </div>

      </div>
    </div>
  );
}

export default MainPage;
