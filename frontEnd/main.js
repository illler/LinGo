const express = require('express');
const http = require('http');
const WebSocket = require('ws');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

const messages = [];
const clients = new Map();

wss.on('connection', (ws) => {
    console.log('Новое соединение WebSocket');

    ws.on('message', (message) => {
        const parsedMessage = JSON.parse(message);

        if (parsedMessage.name && !parsedMessage.text) {
            clients.set(ws, parsedMessage.name);
        } else {
            messages.push(parsedMessage);

            wss.clients.forEach((client) => {
                if (client.readyState === WebSocket.OPEN) {
                    client.send(JSON.stringify(messages));
                }
            });
        }
    });

    ws.send(JSON.stringify(messages));
});

server.listen(8080, () => {
    console.log('Сервер WebSocket запущен на порту 8080');
});
