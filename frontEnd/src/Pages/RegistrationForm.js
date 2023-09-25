import React, { useState } from 'react';
import axios from 'axios';
import {Link, useNavigate} from "react-router-dom";

const RegistrationForm = () => {
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [responseText, setResponseText] = useState('');

    const history = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const requestData = {
            firstname,
            lastname,
            email,
            password,
            role:"USER",
        };
        try {
            const response = await axios.post('http://localhost:8080/api/v1/auth/registration', requestData);
            setResponseText("Вы успешно зарегистрировались!");
            history("/authorization")
        } catch (error) {
            setResponseText('Произошла ошибка: ' + error.message);
        }
    };

    return (
        <div>
            <h1>Регистрация</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Имя:
                    <input type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)} />
                </label>
                <br />
                <label>
                    Фамилия:
                    <input type="text" value={lastname} onChange={(e) => setLastname(e.target.value)} />
                </label>
                <br />
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
                <button type="submit">Зарегистрироваться</button>
            </form>
            <div>{responseText}</div>
        </div>
    );
};

export default RegistrationForm;
