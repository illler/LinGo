import React, {useCallback, useEffect, useRef, useState} from "react"
import API from "../Actions/API";
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import styled from "styled-components"
import Contacts from "../components/Contacts";
import Welcome from "../components/Welcome";
import ChatContainer from "../components/ChatContainer";
import SockJS from 'sockjs-client';
import {over} from "stompjs";
import Logout from "../components/Logout";
import { useTranslation } from 'react-i18next';
import i18n from 'i18next';


export default function Chat(){
    const navigate = useNavigate();
    const authToken = localStorage.getItem('authToken');
    const [contacts, setContacts] = useState([]);
    const [currentUser, setCurrentUser] = useState(undefined);
    const [currentChat, setCurrentChat] = useState(undefined);
    const [isLoaded, setIsLoaded] = useState(false)
    const location = useLocation();
    const currentChatRef = useRef(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchedUsers, setSearchedUsers] = useState([]);
    const { t } = useTranslation();


    useEffect(() => {
        async function fetchData() {
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
                    setIsLoaded(true);
                    console.log(response.data)
                    const userLanguage = response.data.lang;

                    if (userLanguage) {
                        const simplifiedLanguage = userLanguage.split('-')[0];
                        i18n.changeLanguage(simplifiedLanguage)
                            .then(() => console.log('Language changed successfully'))
                            .catch(error => console.error('Error changing language:', error));
                    }

                    console.log(response.data);
                    console.log('localStorage:', localStorage);

                } catch (error) {
                    console.error('Ошибка при получении данных:', error);
                }
            }
        }

        fetchData();
    }, [navigate, authToken, setCurrentUser]);


    // useEffect( () => {
    //     if(currentUser){
    // // Возможно что хост будет в другом формате, но вроде не должен я его из config вытаскиваю
    //         socket.current = io(host)
    //         socket.current.emit("add-user", currentUser.id);
    //
    //     }
    //
    // }, [currentUser])

    useEffect(() => {
        async function fetchData() {
            if (currentUser) {
                const response = await axios.get(API.MESSAGE.RecieveAllCorrespondence, {
                    params: {
                        senderId: currentUser.id
                    },
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                    },
                });
                setContacts((prevContacts) => {
                    const updatedContacts = [...prevContacts];

                    const isTargetUserInContacts = prevContacts.some(
                        (contact) => contact.id === location.state?.targetUser?.id
                    );

                    if (location.state?.targetUser && !isTargetUserInContacts && !updatedContacts.some(contact => contact.id === location.state.targetUser.id)) {
                        updatedContacts.push(location.state.targetUser);

                        navigate(location.pathname, { state: { targetUser: null } });
                    }

                    response.data.forEach((contact) => {
                        const isContactInContacts = updatedContacts.some((existingContact) => existingContact.id === contact.id);
                        if (!isContactInContacts) {
                            updatedContacts.push(contact);
                        }
                    });

                    return updatedContacts;
                });

                if (!currentChat && currentChatRef.current) {
                    setCurrentChat(currentChatRef.current);
                }
            }
        }
        setContacts([]);
        fetchData();
    }, [currentUser]);

    const handleChatChange = (chat) =>
    {
        setCurrentChat(chat);
        currentChatRef.current = chat;
    }

    const handleSearch = async () => {
        const authToken = localStorage.getItem("authToken");

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

            setSearchedUsers(response.data);
        } catch (error) {
            console.error("Error searching users:", error);
        }
    };

    useEffect(() => {
        const { state } = location;
        if (state && state.targetUser && state.targetUser !== currentChatRef.current) {
            currentChatRef.current = state.targetUser;
        }
    }, [location, currentChatRef]);

    const addToContacts = async (user) => {
        const isAlreadyInContacts = contacts.some((contact) => contact.id === user.id);
        handleChatChange(user)
        if (isAlreadyInContacts) {
            console.log("User is already in contacts.");
            return;
        }

        setContacts((prevContacts) => {
            return [...prevContacts, user];
        });

        setSearchedUsers([]);
    };



    const handleCurrentProfile = (currentUser) => {
        navigate(`/profile/${currentUser.id}`, { state: {contact: currentUser} });
    }

    return(
        <Container>{t('chat')}
            <SearchInput
                type="text"
                placeholder={t('searchUsers')}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button onClick={handleSearch}>{t('search')}</button>

            {searchedUsers.length > 0 && (
                <SearchedUsersContainer>
                    <p>{t('Searched Users:')}</p>
                    {searchedUsers.map((user) => (
                        <div key={user.id}>
                            {user.firstname} {user.lastname}
                            <button onClick={() => addToContacts(user)}>{t('write message')}</button>
                        </div>
                    ))}
                </SearchedUsersContainer>
            )}

            <Logout></Logout>

            <div className="container">
                <Contacts contacts={contacts} currentUser={currentUser} changeChat={handleChatChange}/>
                {
                    isLoaded && currentChat === undefined ? (
                    <Welcome currentUser = {currentUser}/>
                    ) : (
                     <ChatContainer
                         currentChat = {currentChat}
                         currentUser = {currentUser}
                     />
                    )
                }
            </div>
            <ButtonContainer>
                <button onClick={() => handleCurrentProfile(currentUser)}>{t('goProfile')}</button>
            </ButtonContainer>
        </Container>

    )
}


const Container = styled.div`
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 1rem;
  align-items: center;
  background-color: #131324;
  .container {
    height: 85vh;
    width: 85vw;
    background-color: #00000076;
    display: grid;
    grid-template-columns: 25% 75%;
    @media screen and (min-width: 720px) and (max-width: 1080px) {
      grid-template-columns: 35% 65%;
    }
  }`;

const SearchInput = styled.input`
  width: 10%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  font-size: 1rem;
`;

const SearchedUsersContainer = styled.div`
  p {
    font-weight: bold;
    color: white;
  }

  div {
    margin-bottom: 1rem;
    display: flex;
    align-items: center;
    color: white;

    button {
      background-color: #4caf50;
      color: white;
      padding: 0.5rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
  }
`;

const ButtonContainer = styled.div`
  margin-top: auto;
  align-self: flex-start;
  margin-left: 100px;
  margin-bottom: 20px;
  button {
    background-color: #4caf50;
    color: white;
    padding: 0.7rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
`;