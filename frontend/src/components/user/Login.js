import React, { useState } from 'react';
import './Login.css';

const Login = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
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
      const response = await fetch('http://localhost:8080/api/auth/login', {
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
      localStorage.setItem('token', result.token);
      alert('로그인 성공!');
      window.location.href = '/main';
    } catch (error) {
      console.error('Error:', error);
      alert('로그인 중 오류가 발생했습니다: ' + error.message);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <h2 className="login-title">로그인</h2>
        <p className="login-description">
          로그인 아이디와 비밀번호를 입력하신 후<br />
          <strong>로그인</strong> 버튼을 클릭하세요.
        </p>
        <div className="login-input-container">
          <input
            type="text"
            name="username"
            placeholder="아이디를 입력하세요"
            value={formData.username}
            onChange={handleChange}
            required
            className="login-input"
          />
        </div>
        <div className="login-input-container">
          <input
            type="password"
            name="password"
            placeholder="비밀번호를 입력하세요"
            value={formData.password}
            onChange={handleChange}
            required
            className="login-input"
          />

        </div>
        <button type="submit" className="login-button">로그인</button>
        <div className="login-footer">
          <a href="/forgot-id">아이디</a>
          <a href="/forgot-password">/비밀번호 찾기</a> | <a href="/signup">회원가입</a>
        </div>
      </form>

    </div>
  );
};

export default Login;
