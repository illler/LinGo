import React, { useEffect, useState } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import styled from "styled-components";
import API from "../Actions/API";

export default function Profile() {
    const navigate = useNavigate();
    const [profileUser, setProfileUser] = useState(undefined);
    const location = useLocation();
    const authToken = localStorage.getItem('authToken');
    const [currentUser, setCurrentUser] = useState(undefined);
    const [friendList, setFriendList] = useState([]);

    const [isAlreadyFriend, setIsAlreadyFriend] = useState(false);

    useEffect(() => {
        async function checkFriendStatus() {
            if (!authToken || !currentUser || !profileUser) {
                return;
            }

            const checkUrl = API.Friends.FriendsCheck;

            try {
                const response = await axios.get(checkUrl, {
                    params: {
                        currentUserId: currentUser.id,
                        newFriendId: profileUser.id,
                    },
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                });

                setIsAlreadyFriend(response.data);
            } catch (error) {
                console.error("Error checking friend status:", error);
            }
        }

        checkFriendStatus();
    }, [authToken, currentUser, profileUser]);

    useEffect(() => {
        async function fetchCurrentUser() {
            if (localStorage.getItem("authToken") === null) {
                navigate("/login");
            } else {
                try {
                    const response = await axios.get(API.USER.GET_INFO, {
                        headers: {
                            'Authorization': `Bearer ${authToken}`,
                        },
                    });
                    setCurrentUser({
                        id: response.data.id,
                        firstname: response.data.firstname,
                        lastname: response.data.lastname,
                    });
                } catch (error) {
                    console.error('Ошибка при получении данных:', error);
                }
            }
        }

        fetchCurrentUser();
    }, [navigate, authToken]);

    useEffect(() => {
        async function fetchUserProfile() {
            const authToken = localStorage.getItem("authToken");
            if (!authToken) {
                navigate("/login");
                return;
            }

            try {
                const response = await axios.get(
                    API.USER.SEARCH_USER,
                    {
                        params: {
                            pattern: location.state.contact.email,
                        },
                        headers: {
                            Authorization: `Bearer ${authToken}`,
                        },
                    }
                );

                if (response.data.length > 0) {
                    const firstUserData = response.data[0];

                    setProfileUser({
                        id: firstUserData.id,
                        firstname: firstUserData.firstname,
                        lastname: firstUserData.lastname,
                        email: firstUserData.email,
                    });
                } else {
                    console.log("Пользователь не найден");
                }
            } catch (error) {
                console.error("Ошибка при получении данных пользователя:", error);
            }
        }

        fetchUserProfile();
    }, [navigate, authToken, location.state.contact.email]);

    useEffect(() => {
        async function fetchFriendList() {
            const authToken = localStorage.getItem("authToken");

            if (!authToken || !currentUser || !profileUser || profileUser.id !== currentUser.id) {
                return;
            }

            const userId = currentUser.id;
            const url = API.Friends.RetrieveAllFriends;

            try {
                const response = await axios.get(url, {
                    params: { userId },
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                });
                setFriendList(response.data);
            } catch (error) {
                console.error("Error fetching friend list:", error);
            }

        }

        fetchFriendList();
    }, [authToken, currentUser, profileUser]);

    const addFriend = async () => {
        const authToken = localStorage.getItem("authToken");

        if (!authToken) {
            navigate("/login");
            return;
        }

        try {
            const response = await axios.post(
                API.Friends.AddFriends,
                null,
                {
                    params: {
                        currentUserId: currentUser.id,
                        newFriendId: profileUser.id,
                    },
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                }
            );

            if (response.status === 200) {
                console.log("Friend added successfully!");
                setIsAlreadyFriend(true)
            } else {
                console.log("Failed to add friend");
            }
        } catch (error) {
            console.error("Error adding friend:", error);
        }
    };

    return (
        <ProfileContainer>
            {profileUser ? (
                <ProfileCard>
                    <Avatar src="logo.svg" alt="User Avatar" />
                    <UserInfo>
                        <h2>{`
                        ${profileUser.firstname} 
                        ${profileUser.lastname}
                        ${profileUser.email}
                      
                        `
                        }
                        </h2>
                        {currentUser && profileUser.id === currentUser.id && (
                            <>
                                <p>Your friend list:</p>
                                {friendList.map((friend) => (
                                    <div key={friend.id}>
                                        {friend.firstname} {friend.lastname}
                                    </div>
                                ))}
                            </>
                        )}
                        {currentUser && profileUser.id !== currentUser.id && (
                            <>
                                {isAlreadyFriend ? (
                                    <p>You are friends!</p>
                                ) : (
                                    <AddFriendButton onClick={addFriend}>Add Friend</AddFriendButton>
                                )}
                            </>
                        )}
                    </UserInfo>
                </ProfileCard>
            ) : (
                <p>Loading...</p>
            )}
            <div>
                <h2>Your profile</h2>
                {currentUser ? (
                    <h3>{`
                    ${currentUser.firstname} 
                    ${currentUser.lastname}
                `}</h3>
                ) : (
                    <p>Loading current user...</p>
                )}
            </div>
        </ProfileContainer>
    );
}

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

const AddFriendButton = styled.button`
  background-color: #4caf50;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;