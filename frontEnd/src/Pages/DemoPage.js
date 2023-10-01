import React, { useEffect } from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import API from "../Actions/API"


const DemoPage = () => {
    const location = useLocation();
    const authToken = location.state?.authToken;
    // console.log(authToken);

    useEffect(() => {
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
    }, [authToken]);

    return (
        <div>
            <h1>Demo Page</h1>
            {authToken ? <p>что-то тут будет</p> : <p>Ошибка авторизации: Токен не получен.</p>}
        </div>
    );
};

export default DemoPage;
