import React, { useState } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const PasswordUpdate = () => {
    const [email, setEmail] = useState('');
    const [tempPassword, setTempPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [isTempPasswordSent, setIsTempPasswordSent] = useState(false);
    const [userId, setUserId] = useState(null);
    const [isTempPasswordVerified, setIsTempPasswordVerified] = useState(false);
    const navigate = useNavigate();



    const handleSendTempPassword = async () => {
        try {
            const recoverPasswordResponse = await axios.post(`http://localhost:8080/api/v1/auth/recover-password?email=${email}`);
            const userData = recoverPasswordResponse.data;

            console.log('Retrieved User Data:', userData);

            const retrievedUserId = userData.id;

            setUserId(retrievedUserId);

            setIsTempPasswordSent(true);
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };

    const handleVerifyTempPassword = async () => {
        try {
            const verifyTempPasswordResponse = await axios.post(
                `http://localhost:8080/api/v1/auth/${userId}/checkTmpPassword`,
                {
                    email: email,
                    password: tempPassword,
                }
            );

            console.log(verifyTempPasswordResponse.data);

            if (verifyTempPasswordResponse.data === true) {
                setIsTempPasswordVerified(true);
            } else {
                console.error('Temporary password verification failed:');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };


    const handleUpdatePassword = async () => {
        try {
            console.log('Email:', email);
            console.log('New Password:', newPassword);
            const updatePasswordResponse = await axios.post('http://localhost:8080/api/v1/auth/updatePassword', {
                email: email,
                password: newPassword,
            });
            console.log(updatePasswordResponse.data)
            if(updatePasswordResponse.data === "Пароль обновлен"){
                navigate("/login")
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };


    return (
        <div>
            <label>Email:</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />

            {!isTempPasswordSent && (
                <button onClick={handleSendTempPassword}>Send Temporary Password</button>
            )}

            {isTempPasswordSent && !isTempPasswordVerified && (
                <div>
                    <label>Temporary Password:</label>
                    <input
                        type="text"
                        value={tempPassword}
                        onChange={(e) => setTempPassword(e.target.value)}
                    />
                    <button onClick={handleVerifyTempPassword}>
                        Verify Temporary Password
                    </button>
                </div>
            )}

            {isTempPasswordVerified && (
                <div>
                    <label>New Password:</label>
                    <input type="password"
                           value={newPassword}
                           onChange={(e) => setNewPassword(e.target.value)}
                    />
                    <button onClick={handleUpdatePassword}>Update Password</button>
                </div>
            )}
        </div>
    );
};

export default PasswordUpdate;
