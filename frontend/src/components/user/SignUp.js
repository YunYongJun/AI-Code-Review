import React, { useState } from 'react';
import './SignUp.css';

const SignUp = () => {
  const [formData, setFormData] = useState({
    email: '',
    username: '',
    password: '',
    phoneNum: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        let errorMessage;
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || "An error occurred";
        } catch {
          errorMessage = await response.text();
        }
        throw new Error(errorMessage);
      }

      const result = await response.json();
      console.log(result);

      window.location.href = '/main';
    } catch (error) {
      console.error('Error:', error);
      alert('회원가입 중 오류가 발생했습니다: ' + error.message);
    }
  };

  return (
    <div className="signup-background">
      <form onSubmit={handleSubmit} className="signup-form">
        <div className="signup-message">
          <h2>회원가입</h2>
          <p>계정이 이미 있는 경우에는 <strong>로그인해주세요.</strong></p>
          <p>
            가입을 하면 AI 코드잼
            <button type="button" onClick={() => alert('이용약관에 대한 내용입니다.')} className="signup-link">이용약관</button>,
            <button type="button" onClick={() => alert('개인정보취급방침에 대한 내용입니다.')} className="signup-link">개인정보취급방침</button> 및
            <button type="button" onClick={() => alert('개인정보 3자 제공에 대한 내용입니다.')} className="signup-link">개인정보 3자 제공</button>에 동의하게 됩니다.
          </p>
          <p>가입 후 아이디는 변경할 수 없습니다.</p>
        </div>

        {/* 사용자 이름 입력 필드 */}
        <div className="signup-input-container">
          <label>사용자 이름</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
            className="signup-input"
          />
        </div>

        {/* 이메일 입력 필드 */}
        <div className="signup-input-container">
          <label>이메일</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            className="signup-input"
          />
        </div>

        {/* 비밀번호 입력 필드 */}
        <div className="signup-input-container">
          <label>비밀번호</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            className="signup-input"
          />
        </div>

        {/* 전화번호 입력 필드 */}
        <div className="signup-input-container">
          <label>전화번호</label>
          <input
            type="text"
            name="phoneNum"
            value={formData.phoneNum}
            onChange={handleChange}
            required
            className="signup-input"
          />
        </div>

        {/* 제출 버튼 */}
        <button type="submit" className="signup-button">회원가입</button>
      </form>
    </div>
  );
};

export default SignUp;
