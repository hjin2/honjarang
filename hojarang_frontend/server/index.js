const express = require('express');
const multer = require('multer');
const AWS = require('aws-sdk');
const { v4: uuidv4 } = require('uuid');
const dotenv = require('dotenv')
dotenv.config()
const app = express();
const port = 5000;
const cors = require('cors');
app.use(cors())

// AWS S3 설정
const s3 = new AWS.S3({
  accessKeyId: process.env.AWS_ACCESS_KEY,
  secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
  region: process.env.AWS_REGION
});

// Multer 설정
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

// 이미지 업로드 라우트
app.post('/upload', upload.single('image'), (req, res) => {
  const file = req.file;
  if (!file) {
    return res.status(400).json({ error: '업로드할 파일을 찾을 수 없습니다.' });
  }
  console.log(file)
  const params = {
    Bucket: process.env.AWS_S3_BUCKET,
    Key: `${uuidv4()}_${file.originalname}`,
    Body: file.buffer,
    ACL: 'public-read'
  };
  // console.log(file.originalname)

  // S3 ManagedUpload with callbacks are not supported in AWS SDK for JavaScript (v3).
  // Please convert to 'await client.upload(params, options).promise()', and re-run aws-sdk-js-codemod.
  s3.upload(params, (err, data) => {
    if (err) {
      console.error('S3 업로드 실패:', err);
      return res.status(500).json({ error: 'S3 업로드 실패' });
    }
    console.log('S3 업로드 성공:', data.Location);
    return res.status(200).json({ url: data.Location });
  });
});

app.listen(port, () => {
  console.log(`서버가 http://localhost:${port} 에서 실행 중입니다.`);
});
