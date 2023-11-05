import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import API from "../Actions/API"

const DemoPage = () => {
    const history = useNavigate();
    const authToken = localStorage.getItem('authToken');
    const [isLoggedOut, setIsLoggedOut] = useState(false);

    const handleLogout = async () => {
        try {
            await axios.post(API.USER.AUTH.LOGOUT, null, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                }
            });
            localStorage.removeItem('authToken');
            history("/")
        } catch (error) {
            console.error('Ошибка при выходе:', error);
        }
    };

    useEffect(() => {
        if (!isLoggedOut) {
            const fetchData = async () => {
                try {
                    const response = await axios.get(API.USER.DEMO, {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${authToken}`,
                        }
                    });
                    console.log('Данные:', response.data);
                } catch (error) {
                    console.error('Ошибка при запросе данных:', error);
                }
            };

            fetchData();
        }
    }, [authToken]);

    return (
        <div>
            <h1>Demo Page</h1>
            {authToken ? (
                <div>
                    <button onClick={handleLogout}>Выход</button>
                    <p>что-то тут будет</p>
                </div>
            ) : (
                <p>Ошибка авторизации: Токен не получен.</p>
            )}
        </div>
    );
};

export default DemoPage;
