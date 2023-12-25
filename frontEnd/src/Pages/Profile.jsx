import React, { useEffect, useState } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { useTable } from "react-table";
import API from "../Actions/API";
import { useTranslation } from 'react-i18next';
import i18n from 'i18next';

export default function Profile() {
    const navigate = useNavigate();
    const [profileUser, setProfileUser] = useState(undefined);
    const location = useLocation();
    const authToken = localStorage.getItem('authToken');
    const [currentUser, setCurrentUser] = useState(undefined);
    const [friendList, setFriendList] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchedFriends, setSearchedFriends] = useState([]);

    const [isAlreadyFriend, setIsAlreadyFriend] = useState(false);
    const { t } = useTranslation();

    const [avatarFile, setAvatarFile] = useState(null);
    const [avatarUrl, setAvatarUrl] = useState(null);

    const columns = [
        { Header: "Firstname", accessor: "firstname" },
        { Header: "Lastname", accessor: "lastname" },
        {
            Header: "Actions",
            accessor: "id",
            Cell: ({ row }) => (
                <button onClick={() => navigateToProfile(row.original)}>
                    Go to Profile
                </button>
            ),
        },
    ];




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
                    if (response.data) {
                        setCurrentUser({
                            id: response.data.id,
                            firstname: response.data.firstname,
                            lastname: response.data.lastname,
                            lang: response.data.lang
                        });


                    } else {
                        console.error("Не удалось получить данные текущего пользователя");
                    }
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
                    API.USER.GET_PROFILE_INFO,
                    {
                        params: {
                            id: location.state.contact.id,
                        },
                        headers: {
                            Authorization: `Bearer ${authToken}`,
                        },
                    }
                );
                const firstUserData = response.data

                if (firstUserData) {
                    setProfileUser({
                        id: firstUserData.id,
                        firstname: firstUserData.firstname,
                        lastname: firstUserData.lastname,
                        email: firstUserData.email,
                    });
                } else {
                    console.error("Не удалось получить данные профиля пользователя");
                }
            } catch (error) {
                console.error("Ошибка при получении данных пользователя:", error);
            }
        }

        fetchUserProfile();
    }, [navigate, authToken, location.state.contact.id]);


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

    const removeFriend = async () => {
        const authToken = localStorage.getItem("authToken");

        if (!authToken) {
            navigate("/login");
            return;
        }

        try {
            const response = await axios.delete(API.Friends.RemoveFriend, {
                params: {
                    currentUserId: currentUser.id,
                    friendId: profileUser.id,
                },
                headers: {
                    Authorization: `Bearer ${authToken}`,
                },
            });

            if (response.status === 200) {
                console.log("Friend removed successfully!");
                setIsAlreadyFriend(false);
            } else {
                console.log("Failed to remove friend");
            }
        } catch (error) {
            console.error("Error removing friend:", error);
        }
    };

    const handleSearch = async () => {
        const authToken = localStorage.getItem("authToken");

        if (!searchTerm.trim()) {
            setSearchedFriends([]);
            return;
        }


        try {
            const response = await axios.get(API.USER.SEARCH_USER, {
                params: {
                    pattern: searchTerm,
                    userId: currentUser.id,
                },
                headers: {
                    Authorization: `Bearer ${authToken}`,
                },
            });

            setSearchedFriends(response.data);
        } catch (error) {
            console.error("Error searching friends:", error);
        }
    };
    const navigateToProfile = (friend) => {
        navigate(`/profile/${friend.id}`, { state: { contact: friend } });
    };

    const writeMessage = () => {
        navigate(`/`, { state: { targetUser: profileUser } })
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setAvatarFile(file);
    };

    const uploadAvatar = async () => {
        const formData = new FormData();
        formData.append("multipartFile", avatarFile);

        try {
            const response = await axios.post(API.Files.UPLOAD_FILE, formData, {
                params: {
                    userId: currentUser.id
                },
                headers: {
                    "Content-Type": "multipart/form-data",
                    Authorization: `Bearer ${authToken}`,
                },
            });

            if (response.status === 200) {
                console.log("Avatar uploaded successfully!");
            } else {
                console.log("Failed to upload avatar");
            }
        } catch (error) {
            console.error("Error uploading avatar:", error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(API.Files.GET_FILE, {
                    params: {
                        userId: currentUser.id
                    },
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                    responseType: 'arraybuffer',
                });
                const arrayBufferView = new Uint8Array(response.data);
                const blob = new Blob([arrayBufferView], { type: response.headers['content-type'] });
                const base64String = await new Promise((resolve) => {
                    const reader = new FileReader();
                    reader.onload = () => resolve(reader.result);
                    reader.readAsDataURL(blob);
                });
                const imageBlob = new Blob([arrayBufferView], { type: response.headers['content-type'] });
                const imageObjectURL = URL.createObjectURL(imageBlob);
                console.log('Avatar URL:', imageObjectURL);
                setAvatarUrl(imageObjectURL);
            } catch (error) {
                console.error("Error fetching avatar:", error);
            }
        };

        if (currentUser && currentUser.id) {
            fetchData();
        }
    }, [navigate, authToken, location.state.contact.id, currentUser]);

    useEffect(() => {
        // Assuming currentUser has a language property, replace it with the actual property name
        const userLanguage = currentUser && currentUser.lang;

        if (userLanguage) {
            // Set the language for the current user
            i18n.changeLanguage(userLanguage);
        }
    }, [currentUser]);

    return (
        <ProfileContainer>
            <BackButton onClick={() => navigate("/")}>{t("Back to Chat")}</BackButton>
            {profileUser ? (
                <ProfileCard>
                    {/*<Avatar src="logo.svg" alt="User Avatar" />*/}
                    {/*{avatarUrl && <img src={avatarUrl} alt="User Avatar" />}*/}

                    <UserInfo>
                        <h2>{`
                        ${profileUser.firstname} 
                        ${profileUser.lastname}
                    
                        `
                        }
                        <p>{`${profileUser.email}`}</p>
                        </h2>
                        {currentUser && profileUser.id === currentUser.id && (
                            <>
                                <p>{t("Your friend list:")}</p>
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
                                    <div>
                                        <p>{t("You are friends!")}</p>
                                        <DeleteFriendButton onClick={removeFriend}>
                                            {t("Delete Friend")}
                                        </DeleteFriendButton>
                                    </div>
                                ) : (
                                    <AddFriendButton onClick={addFriend}>{t("Add Friend")}</AddFriendButton>
                                )}
                            </>
                        )}
                    </UserInfo>
                    {/*<AvatarInput*/}
                    {/*    type="file"*/}
                    {/*    accept="image/png, image/jpeg"*/}
                    {/*    onChange={handleFileChange}*/}
                    {/*/>*/}
                    {/*<UploadAvatarButton onClick={uploadAvatar}>Upload Avatar</UploadAvatarButton>*/}
                </ProfileCard>
            ) : (
                <p>Loading...</p>
            )}
            {profileUser && profileUser.id !== currentUser.id && (
                <WriteMessageButton onClick={writeMessage}>
                    {t("write message")}
                </WriteMessageButton>
            )}
            {/*<div>*/}
            {/*    <h2>Your profile</h2>*/}
            {/*    {currentUser ? (*/}
            {/*        <h3>{`*/}
            {/*        ${currentUser.firstname} */}
            {/*        ${currentUser.lastname}*/}
            {/*    `}</h3>*/}
            {/*    ) : (*/}
            {/*        <p>Loading current user...</p>*/}
            {/*    )}*/}
            {/*</div>*/}
            {currentUser && profileUser && profileUser.id === currentUser.id && (
                <>
                    <SearchContainer>
                        <input
                            type="text"
                            placeholder={t("Search friends...")}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <button onClick={handleSearch}>{t('search')}</button>
                    </SearchContainer>

                    {searchedFriends.length > 0 && (
                        <SearchedFriendsContainer>
                            <p>{t("Searched Friends")}</p>
                            {searchedFriends.map((friend) => (
                                <div key={friend.id}>
                                    {friend.firstname} {friend.lastname}
                                    <button onClick={() => navigateToProfile(friend)}>
                                        {t('goProfile')}
                                    </button>
                                </div>
                            ))}
                        </SearchedFriendsContainer>
                    )}
                </>
            )}
        </ProfileContainer>
    );
}

const ProfileContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #131324;
  > * {
    margin-bottom: 1.5rem;
  }
  height: 100vh;
  width: 100vw;
`;

const ProfileCard = styled.div`
  display: flex;
  align-items: center;
  background-color: #00000076;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  color: white;
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
  background-color: #4e0eff;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

const BackButton = styled.button`
  background-color: #9a86f3;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-bottom: 10px;
`;

const SearchContainer = styled.div`
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;

  input {
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  button {
    background-color: #9a86f3;
    color: white;
    padding: 0.5rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
`;

const SearchedFriendsContainer = styled.div`
  margin-top: 1.5rem;
  p {
    font-weight: bold;
    margin-bottom: 1rem;
    color: white;
  }

  div {
    margin-bottom: 1rem; 
    display: flex;
    align-items: center;
    color: white;

    button {
      margin-left: 1rem;
      background-color: #4e0eff;
      color: white;
      padding: 0.5rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
  }
`;


const DeleteFriendButton = styled.button`
    background-color: #f44336;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-right: 10px;
`;


const WriteMessageButton = styled.button`
    background-color: #2196f3;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-right: 10px;
`;

const AvatarInput = styled.input`
`;

const UploadAvatarButton = styled.button`
  background-color: #2196f3;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
`;
