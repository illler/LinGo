import React, { useState } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";
import API from "../Actions/API"

const AuthorizationForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [responseText, setResponseText] = useState('');

    const history = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const requestData = {
            email,
            password,
        };
        try {
            const response = await axios.post(API.USER.AUTH.LOGIN, requestData);
            const authToken = response.data.token;
            localStorage.setItem('authToken', authToken);
            history('/chat');
        } catch (error) {
            setResponseText('Произошла ошибка: ' + error.message);
        }
    };

    return (
        <div>
            <h1>Авторизация</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Email:
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </label>
                <br />
                <label>
                    Пароль:
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </label>
                <br />
                <button type="submit">Авторизоваться</button>
            </form>
            <div>{responseText}</div>
        </div>
    );
};

export default AuthorizationForm;
