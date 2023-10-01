import React, { useEffect } from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom';


const DemoPage = () => {
    const location = useLocation();
    const authToken = location.state?.authToken;

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (!authToken) {
                    console.error('Ошибка авторизации: Токен не получен.');
                    return;
                }

                const response = await axios.get("http://localhost:8080/api/v1/demo", {
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
