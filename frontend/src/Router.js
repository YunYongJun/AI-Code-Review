import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import SignUp from './components/SignUp';

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        {/* 회원가입 페이지 경로 */}
        <Route path="/signup" element={<SignUp />} />
        {/* 기본 루트에 다른 컴포넌트가 있을 경우 여기에 추가 가능 */}
      </Routes>
    </Router>
  );
};

export default AppRouter;
