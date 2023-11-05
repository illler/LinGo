import React, { useEffect, useState } from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import axios from "axios";
import API from "../Actions/API";
import ContextMenu from '../Actions/ContextMenu';
import {useNavigate} from "react-router-dom";


var stompClient = null;
const ChatRoom = () => {
    const [privateChats, setPrivateChats] = useState(new Map());
    const [publicChats, setPublicChats] = useState([]);
    const [tab,setTab] =useState("CHATROOM");
    const [userData, setUserData] = useState({
        username: '',
        receivername: '',
        connected: false,
        message: '',
        id: ''
    });
    const authToken = localStorage.getItem('authToken');
    const history = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post(API.USER.AUTH.LOGOUT, null, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                }
            });
            localStorage.removeItem('authToken');
            history("/authorization")
        } catch (error) {
            console.error('Ошибка при выходе:', error);
        }
    };
    useEffect(() => {
        if (localStorage.getItem("authToken") === null) {
            history("/authorization");
        }
        if(!userData.connected){
            connect()
        }
        console.log(userData);
    }, [userData]);


    const updateUserFromAPI = async () => {
        try {
            const response = await axios.get(API.USER.GET_INFO, {
                headers: {
                    'Authorization': `Bearer ${authToken}`,
                },
            });
            const firstname = response.data.firstname;
            const id = response.data.id;
            setUserData({ ...userData, username: firstname, id:  id});
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        updateUserFromAPI();
    }, []);


    const connect = () => {
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = over(Sock);
        stompClient.connect({}, onConnected, onError);
    }


    const onConnected = () => {
        stompClient.subscribe('/chatroom/public', onMessageReceived);
        stompClient.subscribe('/user/'+userData.username+'/private', onPrivateMessage);
        userJoin();
    }

    const userJoin=()=>{
        var chatMessage = {
            senderName: userData.username,
            status:"JOIN"
        };
        setUserData({ ...userData, connected: true });
        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    }


    const onMessageReceived = (payload)=>{
        var payloadData = JSON.parse(payload.body);
        switch(payloadData.status){
            case "JOIN":
                if(!privateChats.get(payloadData.senderName)){
                    privateChats.set(payloadData.senderName,[]);
                    setPrivateChats(new Map(privateChats));
                }
                break;
            case "MESSAGE":
                publicChats.push(payloadData);
                setPublicChats([...publicChats]);
                break;
        }
    }

    const onPrivateMessage = (payload)=>{
        console.log(payload);
        var payloadData = JSON.parse(payload.body);
        if(privateChats.get(payloadData.senderName)){
            privateChats.get(payloadData.senderName).push(payloadData);
            setPrivateChats(new Map(privateChats));
        }else{
            let list =[];
            list.push(payloadData);
            privateChats.set(payloadData.senderName,list);
            setPrivateChats(new Map(privateChats));
        }
    }

    const onError = (err) => {
        console.log(err);

    }

    const handleMessage =(event)=>{
        const {value}=event.target;
        setUserData({...userData,"message": value});
    }
    const sendValue=()=>{
        if (stompClient) {
            var chatMessage = {
                senderName: userData.username,
                message: userData.message,
                status:"MESSAGE"
            };
            console.log(chatMessage);
            stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
            setUserData({...userData,"message": ""});
        }
    }

    const sendPrivateValue=()=>{
        if (stompClient) {
            var chatMessage = {
                senderName: userData.username,
                receiverName:tab,
                message: userData.message,
                status:"MESSAGE"
            };

            if(userData.username !== tab){
                privateChats.get(tab).push(chatMessage);
                setPrivateChats(new Map(privateChats));
            }
            stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
            setUserData({...userData,"message": ""});
        }
    }

    const handleUsername=(event)=>{
        const {value}=event.target;
        setUserData({...userData,"username": value});
    }

    const [contextMenu, setContextMenu] = useState({
        visible: false,
        top: 0,
        left: 0,
        username: '',
    });

    const handleContextMenu = (username, e) => {
        e.preventDefault();
        setContextMenu({
            visible: true,
            top: e.clientY,
            left: e.clientX,
            username: username,
        });
    };

    const closeContextMenu = () => {
        setContextMenu({
            ...contextMenu,
            visible: false,
        });
    };

    // const registerUser=()=>{
    //     connect();
    // }
    return (

        <div className="container">
            <button onClick={handleLogout}>Выход</button>

            {userData.connected?
                <div className="chat-box">
                    <div className="member-list">
                        <ul>
                            <li onClick={() => { setTab("CHATROOM") }} className={`member ${tab === "CHATROOM" && "active"}`}>Chatroom</li>
                            {[...privateChats.keys()].map((name, index) => (
                                <li
                                    key={index}
                                    onContextMenu={(e) => handleContextMenu(name, e)}
                                    onClick={() => { setTab(name) }}
                                    className={`member ${tab === name && "active"}`}
                                >
                                    {name}
                                </li>
                            ))}
                        </ul>
                    </div>
                    <ContextMenu
                        visible={contextMenu.visible}
                        top={contextMenu.top}
                        left={contextMenu.left}
                        username={contextMenu.username}
                        onClose={closeContextMenu}
                    />
                    {tab==="CHATROOM" && <div className="chat-content">
                        <ul className="chat-messages">
                            {publicChats.map((chat,index)=>(
                                <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>
                                    {chat.senderName !== userData.username && <div className="avatar">{chat.senderName}</div>}
                                    <div className="message-data">{chat.message}</div>
                                    {chat.senderName === userData.username && <div className="avatar self">{chat.senderName}</div>}
                                </li>
                            ))}
                        </ul>

                        <div className="send-message">
                            <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} />
                            <button type="button" className="send-button" onClick={sendValue}>send</button>
                        </div>
                    </div>}
                    {tab!=="CHATROOM" && <div className="chat-content">
                        <ul className="chat-messages">
                            {[...privateChats.get(tab)].map((chat,index)=>(
                                <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>
                                    {chat.senderName !== userData.username && <div className="avatar">{chat.senderName}</div>}
                                    <div className="message-data">{chat.message}</div>
                                    {chat.senderName === userData.username && <div className="avatar self">{chat.senderName}</div>}
                                </li>
                            ))}
                        </ul>

                        <div className="send-message">
                            <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} />
                            <button type="button" className="send-button" onClick={sendPrivateValue}>send</button>
                        </div>
                    </div>}
                </div>
                :
                <div className="register">
                    <input
                        id="user-name"
                        placeholder="Enter your name"
                        name="userName"
                        value={userData.username}
                        onChange={handleUsername}
                        hidden={true}
                    />
                    {/*<button type="button" onClick={registerUser}>*/}
                    {/*    connect*/}
                    {/*</button>*/}
                </div>}
        </div>
    )
}

export default ChatRoom