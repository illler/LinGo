import React, { Component, useEffect, useState } from 'react';
import axios from 'axios';
import API from "../Actions/API";

const Profile = () => {
    const [userData, setUserData] = useState({
        username: '',
        lastname: '',
        email: '',
    });

    useEffect(() => {
        const authToken = localStorage.getItem('authToken');

        const updateUserFromAPI = async () => {
            try {
                const response = await axios.get(API.USER.GET_INFO, {
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                    },
                });
                const { firstname, lastname,  email} = response.data;
                setUserData({
                    username: firstname,
                    lastname: lastname,
                    email: email
                });
            } catch (error) {
                console.error(error);
            }
        };

        updateUserFromAPI();
    }, []);

    return (
        <div className="profile">
            <h2>Профиль пользователя {userData.username}</h2>
            <p>Фамилия: {userData.lastname}</p>
            <p>Email: {userData.email}</p>
        </div>
    );
};

export default Profile;
