import React from "react";
import {useNavigate} from "react-router-dom";
import styled from "styled-components";
import axios from "axios";
import {BiPowerOff} from 'react-icons/bi'
import API from "../Actions/API";
export default function Logout() {
    const navigate = useNavigate();
    const handleClick = async () => {
        try {
            await axios.post(API.USER.AUTH.LOGOUT, null, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                }
            });
            localStorage.clear();
            navigate("/login")
        } catch (error) {
            console.error('Ошибка при выходе:', error);
        }
    };

    return (
        <Button onClick={handleClick}>
            <BiPowerOff/>
        </Button>
    );
}

const Button = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0.5rem;
  border-radius: 0.5rem;
  background-color: #9a86f3;
  border: none;
  cursor: pointer;
  svg {
    font-size: 1.3rem;
    color: #ebe7ff;
  }`;
