// index.js
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config(); // .env 파일에서 환경변수 로드

const app = express();
const port = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(express.json());

// MongoDB 연결
mongoose.connect(process.env.MONGODB_URI, { 
  useNewUrlParser: true, 
  useUnifiedTopology: true 
})
.then(() => console.log('MongoDB connected'))
.catch(err => console.error('MongoDB connection error:', err));

// 기본 라우트
app.get('/', (req, res) => {
  res.send('Hello from the backend API!');
});

// 서버 시작
app.listen(port, () => {
  console.log(`Server is running on port: ${port}`);
});
