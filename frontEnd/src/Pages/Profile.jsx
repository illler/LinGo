import React, { useEffect, useState } from "react";
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import styled from "styled-components";
import API from "../Actions/API";

export default function Profile() {
    const navigate = useNavigate();
    const [currentUser, setCurrentUser] = useState(undefined);
    const location = useLocation();

    useEffect(() => {
        async function fetchData() {
            const authToken = localStorage.getItem("authToken");
            if (!authToken) {
                navigate("/login");
                return;
            }

            try {
                const queryParams = location.state.contact.email;
                const url = `http://localhost:8080/api/v1/search?pattern=${queryParams}`;
                const response = await axios.get(url, {
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                });
                console.log(response.data[0]);

                if (response.data.length > 0) {
                    const firstUserData = response.data[0];

                    setCurrentUser({
                        id: firstUserData.id,
                        firstname: firstUserData.firstname,
                        lastname: firstUserData.lastname,
                        email: firstUserData.email,
                    });

                    console.log(currentUser);
                } else {
                    console.log("Пользователь не найден");
                }
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

