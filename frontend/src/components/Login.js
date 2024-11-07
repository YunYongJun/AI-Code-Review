import React, { useState } from 'react';

const Login = () => {
  // State for managing input data
  const [formData, setFormData] = useState({
    email: '',
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
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(formData); // You can replace this with API call logic
  };

  return (
    <div style={containerStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <h2 style={titleStyle}>로그인</h2>
        <div style={inputContainerStyle}>
          <label>이메일</label>
          <input
            type="email"
            name="email"
            value={formData.email}
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
