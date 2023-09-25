import React, { useState } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const RegistrationForm = () => {
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
            const response = await axios.post('http://localhost:8080/api/v1/auth/authenticate', requestData);
            console.log(response)
            setResponseText("Вы успешно авторизировались!");
            history("/")

        } catch (error) {
            setResponseText('Произошла ошибка: ' + error.message);
        }
    };

    return (
        <div>
            <h1>Автоизация</h1>
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
                <button type="submit">Авторизироваться</button>
            </form>
            <div>{responseText}</div>
        </div>
    );
};

export default RegistrationForm;
