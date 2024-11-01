import React, { useEffect, useState } from 'react';
import './GradingPage.css';

function GradingPage() {
  const [loading, setLoading] = useState(true);
  const [submittedCode] = useState(`public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}`);

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
      // 로딩이 끝나면 페이지 이동
      window.location.href = '/submitted-codes';
    }, 2500);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div className="gpg-grading-page">
      <header className="gpg-header">
        <h1 className="gpg-title">채점 중입니다.</h1>
      </header>

      <div className="gpg-form-container">
        {loading ? (
          <div className="loader"></div>
        ) : (
          <div className="gpg-code-section">
            <h3>제출한 코드</h3>
            <div className="gpg-code-display">
              <pre><code>{submittedCode}</code></pre>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default GradingPage;
