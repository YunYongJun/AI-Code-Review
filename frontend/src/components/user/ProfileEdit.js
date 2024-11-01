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
            className="profileEdit-input"
          />
        </div>
        <button type="submit" className="profileEdit-button">제출</button>
      </form>
    </div>
  );
};

<<<<<<< HEAD:frontend/src/components/ProfileEdit.js
// 스타일 정의
const containerStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  width: '100%',
  maxWidth: '400px',
  padding: '20px',
  backgroundColor: '#FFFFFF', // 배경색을 흰색으로 변경
  borderRadius: '10px',
  boxShadow: '0 4px 20px rgba(0, 0, 0, 0.1)',
  margin: 'auto',
  marginTop: '50px',
};

const titleStyle = {
  marginBottom: '20px',
  fontSize: '24px',
  textAlign: 'center',
  color: '#333',
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  width: '100%',
};


const inputContainerStyle = {
  marginBottom: '15px',
};


const inputStyle = {
  padding: '10px',
  borderRadius: '5px',
  border: '1px solid #ccc',
  width: '100%',
  boxSizing: 'border-box',
  transition: 'border-color 0.3s',
  fontSize: '16px',
};

const buttonStyle = {
  backgroundColor: '#000',
  color: 'white',
  padding: '10px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
  fontSize: '16px',
};

=======
>>>>>>> 4afb417c171383afc2c7cb87dc76404ef76dbbfd:frontend/src/components/user/ProfileEdit.js
export default ProfileEdit;
