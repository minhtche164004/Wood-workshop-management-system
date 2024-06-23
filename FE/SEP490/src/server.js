const express = require('express');
const multer = require('multer');
const cors = require('cors');
const path = require('path');
const fs = require('fs');

const app = express();
app.use(cors());

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    const dir = path.join(__dirname, '../../FE/SEP490/src/assets/product_images');
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
    cb(null, dir);
  },
  filename: (req, file, cb) => {
    cb(null, Date.now() + '-' + file.originalname);
  }
});

const upload = multer({ storage: storage });

app.post('/api/auth/product/upload', upload.single('file'), (req, res) => {
  res.send({ message: 'File uploaded successfully', filePath: `/assets/image/${req.file.filename}` });
});

app.listen(8080, () => {
  console.log('Server is running on http://localhost:8080');
});
