const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());

app.post('/', (req, res) => {
    res.json({ text: 'Привет от сервера!' });
});

app.listen(3001, () => {
    console.log('Сервер запущен на порту 3001');
});