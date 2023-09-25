import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
    return (
        <div>
            <h1>Домашняя страница</h1>
            <Link to="/registration">Перейти к регистрации</Link>
            <br />
            <Link to="/authorization">Перейти к авторизации</Link>
        </div>
    );
};

export default HomePage;
