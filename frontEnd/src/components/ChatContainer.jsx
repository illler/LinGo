import React, { useState, useEffect } from "react";
import styled from "styled-components";
import Logout from "./Logout";
import ChatInput from "./ChatInput";
import axios from "axios";
import API from "../Actions/API";

export default function ChatContainer({currentChat, currentUser}) {

    const [messages, setMessages] = useState([])
    useEffect(() => {
        async function fetchMessages(senderId, recipientId) {
            try {
                const queryParams = new URLSearchParams({
                    senderId,
                    recipientId,
                }).toString();
                const url = `http://localhost:8080/api/v1/receive-all-message?${queryParams}`;

                const response = await axios.get(url, {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                    }
                });
                setMessages(response.data);
            } catch (error) {
                console.error('Ошибка при получении сообщений:', error);
            }
        }

        if (currentChat) {
            const senderId = currentUser.id;
            const recipientId = currentChat.id;
            fetchMessages(senderId, recipientId);
        }
    }, [currentChat, currentUser]);

    const handleSendMsg = async (msg) => {
        await axios.post(API.MESSAGE.SendMessage, {
            senderId: currentUser.id,
            recipientId: currentChat.id,
            message: msg
        },
            {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                }
            }
        )
    }

    return (
        <>
            { currentChat && (
                <Container>
                    <div className="chat-header">
                        <div className="user-details">
                            <div className="username">
                                <h3>{currentChat.firstname + " " + currentChat.lastname}</h3>
                            </div>
                        </div>
                        <Logout />
                    </div>
                    <div className="chat-messages">
                        {
                            messages.map((message, index) => (
                                    <div key={index}>
                                        <div className={`message ${message.fromSelf === 'from' ? 'from' : 'to'}`}>
                                            <div className="content">
                                                <p>{message.message}</p>
                                            </div>
                                        </div>
                                    </div>
                            ))
                        }
                    </div>
                    <ChatInput handleSendMsg={handleSendMsg}/>
                </Container>
                )
            }
        </>
    );
}

const Container = styled.div`
  display: grid;
  grid-template-rows: 10% 80% 10%;
  gap: 0.1rem;
  overflow: hidden;
  @media screen and (min-width: 720px) and (max-width: 1080px) {
    grid-template-rows: 15% 70% 15%;
  }
  .chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 2rem;
    .user-details {
      display: flex;
      align-items: center;
      gap: 1rem;
      .avatar {
        img {
          height: 3rem;
        }
      }
      .username {
        h3 {
          color: white;
        }
      }
    }
  }
  .chat-messages {
    padding: 1rem 2rem;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    overflow: auto;
    &::-webkit-scrollbar {
      width: 0.2rem;
      &-thumb {
        background-color: #ffffff39;
        width: 0.1rem;
        border-radius: 1rem;
      }
    }
    .message {
      display: flex;
      align-items: center;
      .content {
        max-width: 40%;
        overflow-wrap: break-word;
        padding: 1rem;
        font-size: 1.1rem;
        border-radius: 1rem;
        color: #d1d1d1;
        @media screen and (min-width: 720px) and (max-width: 1080px) {
          max-width: 70%;
        }
      }
    }
    .from {
      justify-content: flex-end;
      .content {
        background-color: #4f04ff21;
      }
    }
    .to {
      justify-content: flex-start;
      .content {
        background-color: #9900ff20;
      }
    }
  }
`;