import React, { useState } from 'react';
import './ProfileEdit.css';

const ProfileEdit = () => {
  const [formData, setFormData] = useState({
    email: '',
    currentPassword: '',
    newPassword: '',
    phone: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(formData); // 콘솔에 입력 데이터 출력 (추후 API 요청 등 추가 가능)
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
              name="phone"
              value={formData.phone}
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
