import React, { useState } from 'react';

const SignUp = () => {
  // 입력 데이터를 관리하기 위한 state 설정
  const [formData, setFormData] = useState({
    email: '',
    id: '',
    password: '',
    passwordConfirm: '',
    phoneNum: '',
    agreedToPrivacy: false, // 개인정보 수집 동의 여부
  });

  // 에러 메시지 및 상태 관리
  const [errorMessage, setErrorMessage] = useState('');

  // 입력 값이 변경될 때 호출되는 함수
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: type === 'checkbox' ? checked : value, // 체크박스의 경우 체크 여부 처리
    }));
  };

  // 백엔드에 회원가입 데이터를 전송하는 함수
  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼 제출 시 페이지 새로고침 방지

    // 비밀번호 확인이 맞는지 체크
    if (formData.password !== formData.passwordConfirm) {
      setErrorMessage('비밀번호가 일치하지 않습니다.');
      return;
    }

    // 서버로 보낼 데이터 준비
    const requestData = {
      email: formData.email,
      username: formData.id, // 백엔드에서는 `username`으로 사용
      password: formData.password,
      phoneNum: formData.phoneNum,
    };

    try {
      const response = await fetch('/api/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      });

      if (response.ok) {
        // 회원가입 성공 시 처리
        alert('회원가입 성공!');
      } else {
        // 실패 시 에러 메시지 처리
        const errorData = await response.json();
        setErrorMessage(errorData.message || '회원가입에 실패했습니다.');
      }
    } catch (error) {
      console.error('회원가입 요청 중 오류 발생:', error);
      setErrorMessage('서버 요청 중 오류가 발생했습니다.');
    }
  };

  return (
    <div style={backgroundStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        {/* 안내 메시지 추가 */}
        <div style={messageStyle}>
          <h2>회원가입</h2>
          {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>} {/* 에러 메시지 표시 */}
          <p>계정이 이미 있는 경우에는 <strong>로그인해주세요.</strong></p>
          <p>
            가입을 하면 AI 코드잼 <a href="#">이용약관</a>, <a href="#">개인정보취급방침</a> 및 <a href="#">개인정보 3자제공</a>에 동의하게 됩니다.
          </p>
          <p>가입 후 아이디는 변경할 수 없습니다.</p>
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
            style={inputStyle} // 부드러운 스타일 추가
          />
        </div>

        {/* Id 입력 필드 */}
        <div style={inputContainerStyle}>
          <label>아이디</label>
          <input
            type="text"
            name="id"
            value={formData.id}
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

        {/* Password 확인 필드 */}
        <div style={inputContainerStyle}>
          <label>비밀번호 확인</label>
          <input
            type="password"
            name="passwordConfirm"
            value={formData.passwordConfirm}
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

        {/* 개인정보 수집 동의 체크박스 */}
        <div style={inputContainerStyle}>
          <label>
            <input
              type="checkbox"
              name="agreedToPrivacy"
              checked={formData.agreedToPrivacy}
              onChange={handleChange}
              required
            />
            개인정보 수집 동의
          </label>
        </div>

        {/* 제출 버튼 */}
        <button type="submit" style={buttonStyle}>회원가입</button>
      </form>
    </div>
  );
};

// 스타일 설정
const backgroundStyle = {
  backgroundImage: 'url(https://example.com/your-background-image.jpg)', // 배경 이미지 URL
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
  backgroundColor: 'rgba(255, 255, 255, 0.8)', // 배경 반투명
  borderRadius: '10px', // 둥근 모서리 추가
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', // 그림자 추가
};

const inputContainerStyle = {
  marginBottom: '15px',
  display: 'flex',
  flexDirection: 'column',
};

const inputStyle = {
  padding: '10px',
  border: '1px solid #ddd',
  borderRadius: '5px', // 둥근 모서리로 부드러운 느낌
  outline: 'none',
  fontSize: '16px',
  transition: 'border 0.3s ease', // 마우스 올렸을 때의 전환 효과
};

const buttonStyle = {
  backgroundColor: 'black',
  color: 'white',
  padding: '10px',
  border: 'none',
  borderRadius: '5px', // 버튼 모서리 둥글게
  cursor: 'pointer',
  fontSize: '16px',
  transition: 'background-color 0.3s ease',
};

// 메시지 스타일
const messageStyle = {
  marginBottom: '20px',
  textAlign: 'center', // 중앙 정렬
  color: '#333', // 텍스트 색상
};

// hover 스타일
buttonStyle[':hover'] = {
  backgroundColor: '#333', // 마우스 오버 시 색상 변경
};

export default SignUp;