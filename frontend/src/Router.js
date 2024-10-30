import React, { useState } from 'react'; // Import useState to manage submitted codes
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'; 
import SignUp from './components/SignUp'; 
import Login from './components/Login'; 
import ProfileEdit from './components/ProfileEdit'; 
import MainPage from './components/MainPage'; 
import RankingPage from './components/RankingPage'; 
import AchievementPage from './components/AchievementPage'; 
import CodeEditor from './components/CodeEditor'; 
import SubmittedCodes from './components/SubmittedCodes'; // Import the new component
import GradingPage from './components/GradingPage'; // 체점 페이지
import SubmitCodePage from './components/SubmitCodePage';

const AppRouter = () => {
  const [submittedCodes, setSubmittedCodes] = useState([]); // State to store submitted codes

  return (
    <Router>
      <Routes>
        {/* 회원가입 페이지 경로 */}
        <Route path="/signup" element={<SignUp />} /> 

        {/* 로그인 페이지 경로 */}
        <Route path="/login" element={<Login />} /> 

        {/* 메인 페이지 경로 */}
        <Route path="/main" element={<MainPage />} /> 

        {/* 랭킹 페이지 경로 */}
        <Route path="/ranking" element={<RankingPage />} /> 

        {/* 업적 페이지 경로 */}
        <Route path="/achievement" element={<AchievementPage />} /> 

        {/* 코드 제출 페이지 경로 */}
        <Route path="/submission" element={<SubmitCodePage />} /> 

        {/* 제출 코드 목록 페이지 경로 */}
        <Route 
          path="/submitted-codes" 
          element={<SubmittedCodes submittedCodes={submittedCodes} />} 
        />

        {/* 개인정보 수정 페이지 경로 */}
        <Route path="/profile-edit" element={<ProfileEdit />} /> 


        {/* 체점 페이지 경로 추가 */}
        <Route path="/grading" element={<GradingPage />} />
        

        {/* 기본 루트 경로를 메인 페이지로 설정 */}
        <Route path="/" element={<MainPage />} /> 
      </Routes>
    </Router>
  );
};

export default AppRouter;
