import React, {useState, useEffect, useRef} from "react";
import styled from "styled-components";
import Logout from "./Logout";
import ChatInput from "./ChatInput";
import axios from "axios";
import API from "../Actions/API";
import {v4 as uuidv4} from "uuid";
import SockJS from 'sockjs-client';
import {over} from "stompjs";
import {host} from "../config";

export default function ChatContainer({currentChat, currentUser, updateContacts}) {

    const [messages, setMessages] = useState([]);
    const [arrivalMessage, setArrivalMessage] = useState(null)
    const scrollRef = useRef();
    const [stompClient, setStompClient] = useState(null);
    const [stompSubscription, setStompSubscription] = useState(null);
    const [messagesLoaded, setMessagesLoaded] = useState(null)


    const [showOriginalMap, setShowOriginalMap] = useState({});


    useEffect(() => {
        if (currentChat) {
            async function fetchMessages(senderId, recipientId) {
                try {
                    const queryParams = new URLSearchParams({
                        senderId,
                        recipientId,
                    }).toString();
                    const url = `${API.MESSAGE.GetAllMessages}?${queryParams}`;

                    const response = await axios.get(url, {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${localStorage.getItem('authToken')}`,
                        }
                    });
                    setMessages(response.data);
                    setMessagesLoaded(true);
                } catch (error) {
                    console.error('Ошибка при получении сообщений:', error);
                }
            }

            const senderId = currentUser.id;
            localStorage.setItem('currentId', senderId)
            const recipientId = currentChat.id;
            fetchMessages(senderId, recipientId);
        }
    }, [currentChat, currentUser]);

    useEffect(() => {
        return () => {
            if (stompSubscription) {
                stompSubscription.unsubscribe();
            }
        };
    }, []);

    useEffect(() => {
        if (currentChat && currentUser && currentUser.id && messagesLoaded) {
            const socket = new SockJS(host+"/ws");
            const stomp = over(socket);

            if (stompSubscription) {
                stompSubscription.unsubscribe();
            }

            stomp.connect({}, () => {
                setStompClient(stomp);

                const subscription = stomp.subscribe(
                    `/user/${currentUser.id}/private`,
                    (message) => {
                        const receivedMessage = JSON.parse(message.body);

                        if (receivedMessage.senderId === currentChat.id) {
                            setMessages((prevMessages) => [...prevMessages, receivedMessage]);
                        }
                    }
                );
                setStompSubscription(subscription);
            });
        }

        return () => {
            if (stompSubscription) {
                stompSubscription.unsubscribe();
            }
        };
    }, [currentChat, currentUser, messagesLoaded]);


    const handleSendMsg = async (msg) => {
        const newMessage = {
            id: uuidv4(),
            senderId: currentUser.id,
            translatedMessage: msg,
        };
        console.log(newMessage)
        setMessages((prevMessages) => [...prevMessages, newMessage]);
        setShowOriginalMap(prevState => ({
            ...prevState,
            [newMessage.id]: false,
        }));


        stompClient.send('/app/private-message', {}, JSON.stringify({
            'senderId': currentUser.id,
            'recipientId': currentChat.id,
            'originalMessage': msg,
        }));

        // await axios.post(API.MESSAGE.SendMessage, {
        //     senderId: currentUser.id,
        //     recipientId: currentChat.id,
        //     message: msg,
        // }, {
        //     headers: {
        //         "Content-Type": "application/json",
        //         Authorization: `Bearer ${localStorage.getItem('authToken')}`,
        //     },
        // });


    };

    useEffect(() => {
        if (messagesLoaded) {
            scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
        }
    }, [messagesLoaded, messages]);


    // socket.current.emit("send-msg", {
    //     to: currentUser.id,
    //     from: currentChat.id,
    //     message: msg,
    // })
    // const msgs = [...messages];
    // msgs.push({ fromSelf: "from", message: msg});
    // setMessages(msgs)
    // useEffect(()=>{
    //     if(socket.current){
    //         socket.current.on("msg-recieve", (msg)=>{
    //             setArrivalMessage({fromself: "to", message: msg});
    //         });
    //
    //     }
    // }, []);
    //
    // useEffect(() => {
    //     arrivalMessage && setMessages((prev) => [...prev, arrivalMessage])
    //
    // },[arrivalMessage])

    // useEffect(()=> {
    //     scrollRef.current?.scrollIntoView({behavior: "smooth"})
    // }, [messages]);

    const handleToggleOriginal = (messageId) => {
        setShowOriginalMap(prevState => ({
            ...prevState,
            [messageId]: !prevState[messageId], // Инвертирование состояния для конкретного сообщения
        }));
    };

    return (
        <>
            {currentChat && (
                <Container>
                    <div className="chat-header">
                        <div className="user-details">
                            <div className="username">
                                <h3>{currentChat.firstname + " " + currentChat.lastname}</h3>
                            </div>
                        </div>
                        {/*<Logout/>*/}
                    </div>
                    <div className="chat-messages" ref={scrollRef}>
                        {messages.map((message) => (
                            <div key={message.id}>
                                <div className={`message ${message.userId === localStorage.getItem('currentId') || message.senderId === localStorage.getItem('currentId') ? 'from' : 'to'}`}>
                                    <div className="content">
                                        {showOriginalMap[message.id] ? (
                                            <p>{message.originalMessage}</p>
                                        ) : (
                                            <p>{message.translatedMessage}</p>
                                        )}
                                        {message.senderId !== localStorage.getItem('currentId') && (
                                            <label>
                                                <input type="checkbox" checked={showOriginalMap[message.id]} onChange={() => handleToggleOriginal(message.id)} />
                                                orig
                                            </label>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                        <div ref={scrollRef}></div>
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