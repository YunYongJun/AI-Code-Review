// Footer.js
import React from "react";
import "./Footer.css";

function Footer() {
    return (
        <footer className="footer">
            <div className="footer-content">
                <div className="footer-menuBar-logo">
                    CODE<span className="footer-menuBar-logo-highlight">REVIEW</span>
                    <span className="footer-menuBar-logo-subtext">코드 체점 사이트</span>
                </div>
                <div className="footer-info">
                    <p>팀장: 이현우</p>
                    <p>팀원: 박승아, 박기량, 박성호, 윤용준</p>
                    <p>2팀: Diamond | 코드 체점 사이트 | Email: gusdn1a4@gmail.com</p>
                    <p>&copy; Diamond All rights reserved.</p>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
