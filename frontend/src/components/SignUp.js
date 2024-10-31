import React, { useState } from 'react';

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
        // 응답을 JSON으로 시도하고 실패하면 텍스트로 처리
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

      window.location.href = '/main';
    } catch (error) {
      console.error('Error:', error);
      alert('회원가입 중 오류가 발생했습니다: ' + error.message); // 오류 메시지 출력
    }
  };

  return (
    <div style={backgroundStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <div style={messageStyle}>
          <h2>회원가입</h2>
          <p>계정이 이미 있는 경우에는 <strong>로그인해주세요.</strong></p>
          <p>
            가입을 하면 AI 코드잼 <button type="button" onClick={() => alert('이용약관에 대한 내용입니다.')} style={linkStyle}>이용약관</button>,
            <button type="button" onClick={() => alert('개인정보취급방침에 대한 내용입니다.')} style={linkStyle}>개인정보취급방침</button> 및
            <button type="button" onClick={() => alert('개인정보 3자 제공에 대한 내용입니다.')} style={linkStyle}>개인정보 3자 제공</button>에 동의하게 됩니다.
          </p>
          <p>가입 후 아이디는 변경할 수 없습니다.</p>
        </div>

        {/* Username 입력 필드 추가 */}
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

        {/* Email 입력 필드 */}
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

        {/* Password 입력 필드 */}
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

        {/* 전화번호 입력 필드 */}
        <div style={inputContainerStyle}>
          <label>전화번호</label>
          <input
            type="text"
            name="phoneNum"
            value={formData.phoneNum}
            onChange={handleChange}
            required
            style={inputStyle}
          />
        </div>

        {/* 제출 버튼 */}
        <button type="submit" style={buttonStyle}>회원가입</button>
      </form>
    </div>
  );
};

// 스타일 설정
const backgroundStyle = {
  backgroundImage: 'url(https://example.com/your-background-image.jpg)',
  backgroundSize: 'cover',
  height: '100vh',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  width: '350px',
  padding: '20px',
  backgroundColor: 'rgba(255, 255, 255, 0.8)',
  borderRadius: '10px',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
};

const inputContainerStyle = {
  marginBottom: '15px',
  display: 'flex',
  flexDirection: 'column',
};

const inputStyle = {
  padding: '10px',
  border: '1px solid #ddd',
  borderRadius: '5px',
  outline: 'none',
  fontSize: '16px',
  transition: 'border 0.3s ease',
};

const buttonStyle = {
  backgroundColor: 'black',
  color: 'white',
  padding: '10px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  fontSize: '16px',
  transition: 'background-color 0.3s ease',
};

// 메시지 스타일
const messageStyle = {
  marginBottom: '20px',
  textAlign: 'center',
  color: '#333',
};

// 링크 스타일
const linkStyle = {
  color: 'blue',
  textDecoration: 'underline',
  cursor: 'pointer',
};

export default SignUp;
