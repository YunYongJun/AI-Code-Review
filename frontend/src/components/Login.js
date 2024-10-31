import React, { useState } from 'react';

const Login = () => {
  // State for managing input data
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  // Handle input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData), // formData를 JSON 문자열로 변환
      });

      if (!response.ok) {
        let errorMessage;
        try {
          const errorData = await response.json(); // JSON 형식일 경우
          errorMessage = errorData.message || "An error occurred";
        } catch {
          errorMessage = await response.text(); // 텍스트 형식일 경우
        }
        throw new Error(errorMessage);
      }

      const result = await response.json(); // 성공 시 JSON으로 변환
      console.log(result); // 결과 출력

      alert('로그인 성공!'); // 로그인 성공 알림
      window.location.href = '/main'; // 메인 페이지로 이동
    } catch (error) {
      console.error('Error:', error);
      alert('로그인 중 오류가 발생했습니다: ' + error.message); // 오류 메시지 출력
    }
  };

  return (
    <div style={containerStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <h2 style={titleStyle}>로그인</h2>
        <div style={inputContainerStyle}>
          <label>사용자 이름</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
            style={inputStyle}
          />
        </div>
        <div style={inputContainerStyle}>
          <label>비밀번호</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            style={inputStyle}
          />
        </div>
        <button type="submit" style={buttonStyle}>로그인</button>
        <p style={footerStyle}>회원 가입은 <a href="/signup">여기</a>에서 할 수 있습니다.</p>
      </form>
    </div>
  );
};

// Styles for the components
const containerStyle = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  height: '100vh',
  background: 'url("your-background-image-url") no-repeat center center fixed',
  backgroundSize: 'cover',
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  width: '300px',
  backgroundColor: 'rgba(255, 255, 255, 0.8)',
  padding: '20px',
  borderRadius: '8px',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
};

const titleStyle = {
  textAlign: 'center',
  marginBottom: '20px',
};

const inputContainerStyle = {
  marginBottom: '10px',
};

const inputStyle = {
  padding: '10px',
  borderRadius: '4px',
  border: '1px solid #ccc',
  width: '100%',
  boxSizing: 'border-box',
  transition: 'border-color 0.3s',
};

const buttonStyle = {
  backgroundColor: 'black',
  color: 'white',
  padding: '10px',
  border: 'none',
  borderRadius: '4px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
};

const footerStyle = {
  textAlign: 'center',
  marginTop: '20px',
};

export default Login;
