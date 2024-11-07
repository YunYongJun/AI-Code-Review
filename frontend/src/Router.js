import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import ProfileEdit from './components/ProfileEdit'; // 개인정보 수정 페이지 컴포넌트 추가
import MainPage from './components/MainPage';
import RankingPage from './components/RankingPage';
import AchievementPage from './components/AchievementPage';
import CodeEditor from './components/CodeEditor';


const AppRouter = () => {
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
        <Route path="/submissions" element={<CodeEditor />}/>
        
        {/* 개인정보 수정 페이지 경로 */}
        <Route path="/profile-edit" element={<ProfileEdit />} />
        
        {/* 기본 루트 경로를 로그인 페이지로 설정 */}
        <Route path="/" element={<MainPage />} />

      </Routes>
    </Router>
  );
};

export default AppRouter;

