import React, {useEffect, useRef, useState} from "react"
import API from "../Actions/API";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import styled from "styled-components"
import Contacts from "../components/Contacts";
import Welcome from "../components/Welcome";
import ChatContainer from "../components/ChatContainer";
import SockJS from 'sockjs-client';
import {over} from "stompjs";



export default function Chat(){
    const navigate = useNavigate();
    const authToken = localStorage.getItem('authToken');
    const [contacts, setContacts] = useState([]);
    const [currentUser, setCurrentUser] = useState(undefined);
    const [currentChat, setCurrentChat] = useState(undefined);
    const [isLoaded, setIsLoaded] = useState(false)

    useEffect(() => {
        async function fetchData() {
            console.log(authToken)
            if (localStorage.getItem("authToken") === null) {
                navigate("/login");
            } else {
                try {
                    const response = await axios.get(API.USER.GET_INFO, {
                        headers: {
                            'Authorization': `Bearer ${authToken}`,
                        },
                    });
                    console.log(response.data)
                    setCurrentUser({
                        id: response.data.id,
                        firstname: response.data.firstname,
                        lastname: response.data.lastname,
                    });
                    console.log(currentUser)
                    setIsLoaded(true);
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
                const response = await axios.get(API.USER.GET_ALL_USERS, {
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                    },
                });
                console.log(response.data)
                setContacts(response.data);
            }
        }
        console.log(contacts)
        fetchData();
    }, [currentUser]);

    const handleChatChange = (chat) =>
    {
        setCurrentChat(chat)
    }

    return(
        <Container>Chat
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