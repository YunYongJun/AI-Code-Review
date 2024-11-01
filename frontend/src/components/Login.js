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

      alert('로그인 성공!');
      window.location.href = '/main';
    } catch (error) {
      console.error('Error:', error);
      alert('로그인 중 오류가 발생했습니다: ' + error.message);
    }
  };

  return (
    <div style={containerStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <h2 style={titleStyle}>로그인</h2>
        <p style={descriptionStyle}>
          로그인 아이디와 비밀번호를 입력하신 후<br />
          <strong>로그인</strong> 버튼을 클릭하세요.
        </p>
        <div style={inputContainerStyle}>
          <input
            type="text"
            name="username"
            placeholder="아이디를 입력하세요"
            value={formData.username}
            onChange={handleChange}
            required
            style={inputStyle}
          />
        </div>
        <div style={inputContainerStyle}>
          <input
            type="password"
            name="password"
            placeholder="비밀번호를 입력하세요"
            value={formData.password}
            onChange={handleChange}
            required
            style={inputStyle}
          />
        </div>
        <button type="submit" style={buttonStyle}>로그인</button>
        <div style={linkContainerStyle}>

          <a href="/find-username" style={linkStyle}>아이디 찾기</a>
          <span> | </span>
          <a href="/find-password" style={linkStyle}>비밀번호 찾기</a>
          <span> | </span>
          <a href="/signup" style={linkStyle}>회원가입</a>

        </div>
      </form>
      <footer style={footerStyle}>
        <p>Code Review</p>
        <address style={addressStyle}>
          주소: 서울시 금천구 가산디지털1로 142, 821호<br />
          Tel: 02-851-3093 | Fax: 02-2067-3093 | Email: damdainterior@naver.com
        </address>
      </footer>
    </div>
  );
};

// Styles for the components
const containerStyle = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  height: '100vh',
  backgroundColor: '#f4f4f9',
  fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif',
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  width: '350px',
  backgroundColor: 'white',
  padding: '30px',
  borderRadius: '8px',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
};

const titleStyle = {
  fontSize: '24px',
  fontWeight: 'bold',
  marginBottom: '10px',
};

const descriptionStyle = {
  fontSize: '14px',
  color: '#666',
  marginBottom: '20px',
  textAlign: 'center',
};

const inputContainerStyle = {
  width: '100%',
  marginBottom: '15px',
};

const inputStyle = {
  width: '100%',
  padding: '10px',
  borderRadius: '5px',
  border: '1px solid #ddd',
  boxSizing: 'border-box',
  fontSize: '14px',
};

const buttonStyle = {
  width: '100%',
  padding: '12px',
  backgroundColor: '#2c3e50',
  color: 'white',
  fontSize: '16px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  marginTop: '10px',
  transition: 'background-color 0.3s',
};

const linkContainerStyle = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  marginTop: '15px',
  fontSize: '14px',
};

const linkStyle = {
  color: '#3498db',
  textDecoration: 'none',
  marginRight: '10px',
};

const footerStyle = {
  marginTop: '30px',
  fontSize: '14px',
  color: '#888',
  textAlign: 'center',
};

const addressStyle = {
  fontSize: '12px',
  lineHeight: '1.5',
  marginTop: '5px',
};

export default Login;
