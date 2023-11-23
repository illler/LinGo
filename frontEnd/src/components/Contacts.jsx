import React, {useEffect, useState} from "react";
import styled from "styled-components";
import Logo from "../assets/logo.svg";
import {useNavigate} from "react-router-dom";

export default function Contacts({contacts, currentUser, changeChat}){
    const [currentUserName, setCurrentUserName] = useState(undefined);
    const [currentSelected, setCurrentSelected] = useState(undefined);
    const navigate = useNavigate()
    useEffect(() => {
        if(currentUser){
            const UserName = currentUser.firstname + " " + currentUser.lastname
            setCurrentUserName(UserName);
        }
    }, [currentUser])
    const changeCurrentChat = (index, contact) => {
        setCurrentSelected(index);
        changeChat(contact)
    }

    const handleContextMenu = (event, contact) => {
        event.preventDefault();
        event.stopPropagation();
        setCurrentSelected(undefined);
        event.currentTarget.setAttribute("data-context-menu", "true");
        navigate(`/profile/${contact.id}`, { state: { contact } });
    };

    return <>
        <Container>
            <div className="brand">
                <img src={Logo} alt="logo" />
                <h3>LinGo</h3>
            </div>
            <div className="contacts">
                {contacts.map((contact, index) => {
                    return (
                        <div
                            onContextMenu={(e) => handleContextMenu(e, contact)}
                            className={`contact ${
                                index === currentSelected ? "selected" : ""
                            }`}
                            key={index}
                            onClick={() => changeCurrentChat(index, contact)}
                        >
                            <div className="username">
                                <h3>{contact.firstname + " " +contact.lastname}</h3>
                            </div>
                        </div>
                    );
                })}
            </div>
            <div className="current-user">
                <div className="username">
                    <h2>{currentUserName}</h2>
                </div>
            </div>
        </Container>
        </>;
}

const Container = styled.div`
  display: grid;
  grid-template-rows: 10% 75% 15%;
  overflow: hidden;
  background-color: #080420;
  .brand {
    display: flex;
    align-items: center;
    gap: 1rem;
    justify-content: center;
    img {
      height: 2rem;
    }
    h3 {
      color: white;
      text-transform: uppercase;
    }
  }
  .contacts {
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow: auto;
    gap: 0.8rem;
    &::-webkit-scrollbar {
      width: 0.2rem;
      &-thumb {
        background-color: #ffffff39;
        width: 0.1rem;
        border-radius: 1rem;
      }
    }
    .contact {
      background-color: #ffffff34;
      min-height: 5rem;
      cursor: pointer;
      width: 90%;
      border-radius: 0.2rem;
      padding: 0.4rem;
      display: flex;
      gap: 1rem;
      align-items: center;
      transition: 0.5s ease-in-out;
      .username {
        h3 {
          color: white;
        }
      }
      &:hover {
        background-color: #9a86f3;
      }
    }
    .selected {
      background-color: #9a86f3;
    }
    &[data-context-menu="true"] {
      background-color: #9a86f3;
    }
  }

  .current-user {
    background-color: #0d0d30;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 2rem;
    .username {
      h2 {
        color: white;
      }
    }
    @media screen and (min-width: 720px) and (max-width: 1080px) {
      gap: 0.5rem;
      .username {
        h2 {
          font-size: 1rem;
        }
      }
    }
  }
`;