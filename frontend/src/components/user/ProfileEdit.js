import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import './ProfileEdit.css';

const ProfileEdit = () => {
  const [formData, setFormData] = useState({
    email: '',
    currentPassword: '',
    newPassword: '',
    phoneNum: '', // API 명세에 맞춰 name 설정
  });
  const [userId, setUserId] = useState(null); // 사용자 ID 상태 추가
  const [token, setToken] = useState(''); // JWT 토큰 상태 추가

  useEffect(() => {
    // JWT 토큰에서 사용자 ID 추출
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      const decodedToken = jwtDecode(storedToken);
      setUserId(decodedToken.userId); // 사용자 ID 설정
      setToken(storedToken); // 토큰 설정
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!userId) {
      alert('사용자 ID를 찾을 수 없습니다.');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/auth/update/${userId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`, // JWT 토큰 추가
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const result = await response.json();
        alert(result.message || '사용자 정보가 성공적으로 변경되었습니다.');
      } else {
        alert('사용자 정보를 업데이트하는 중 오류가 발생했습니다.');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="app-container">
      <div className="profileEdit-container">
        <h2 className="profileEdit-title">개인정보 수정</h2>
        <form onSubmit={handleSubmit} className="profileEdit-form">
          <div className="profileEdit-input-container">
            <label>이메일</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="새 이메일 입력"
              className="profileEdit-input"
            />
          </div>
          <div className="profileEdit-input-container">
            <label>기존 비밀번호</label>
            <input
              type="password"
              name="currentPassword"
              value={formData.currentPassword}
              onChange={handleChange}
              required
              placeholder="현재 비밀번호 입력"
              className="profileEdit-input"
            />
          </div>
          <div className="profileEdit-input-container">
            <label>새 비밀번호</label>
            <input
              type="password"
              name="newPassword"
              value={formData.newPassword}
              onChange={handleChange}
              required
              placeholder="새 비밀번호 입력"
              className="profileEdit-input"
            />
          </div>
          <div className="profileEdit-input-container">
            <label>전화번호</label>
            <input
              type="text"
              name="phoneNum" // API 명세에 맞춰 name 설정
              value={formData.phoneNum}
              onChange={handleChange}
              required
              placeholder="새 전화번호 입력"
              className="profileEdit-input"
            />
          </div>
          <button type="submit" className="profileEdit-button">제출</button>
        </form>
      </div>
    </div>
  );
};

export default ProfileEdit;
