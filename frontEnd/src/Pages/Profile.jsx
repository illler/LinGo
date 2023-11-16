import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import API from "../Actions/API";

export default function Profile() {
    const navigate = useNavigate();
    const [currentUser, setCurrentUser] = useState(undefined);

    useEffect(() => {
        async function fetchData() {
            const authToken = localStorage.getItem("authToken");
            if (!authToken) {
                navigate("/login");
                return;
            }

            try {
                const response = await axios.get(API.USER.GET_INFO, {
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                });
                setCurrentUser({
                    id: response.data.id,
                    firstname: response.data.firstname,
                    lastname: response.data.lastname,
                    email: response.data.email
                });
            } catch (error) {
                console.error("Ошибка при получении данных пользователя:", error);
            }
        }

        fetchData();
    }, [navigate]);

    return (
        <ProfileContainer>
            {currentUser ? (
                <ProfileCard>
                    <Avatar src="logo.svg" alt="User Avatar" />
                    <UserInfo>
                        <h2>{`
                        ${currentUser.firstname} 
                        ${currentUser.lastname}
                        ${currentUser.email}
                        
                        `
                        }
                        </h2>
                    </UserInfo>
                </ProfileCard>
            ) : (
                <p>Loading...</p>
            )}
        </ProfileContainer>
    );
};

const ProfileContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const ProfileCard = styled.div`
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
`;

const Avatar = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-right: 20px;
`;

const UserInfo = styled.div`
  h2 {
    margin-bottom: 10px;
  }

  p {
    margin: 5px 0;
    color: #888;
  }
`;

