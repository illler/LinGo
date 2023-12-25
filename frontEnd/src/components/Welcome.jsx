import React from "react";
import styled from "styled-components";
import Robot from "../assets/robot.gif";
import {useTranslation} from "react-i18next";

export default function Welcome({ currentUser }){
    const { t } = useTranslation();

    return (
        <Container>
            <img src={Robot} alt={"Robot"} />
            <h1>
                {t('Welcome')}, <span>{currentUser.firstname}!</span>
            </h1>
            <h3>{t('Started_message')}</h3>
        </Container>
    )
}

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  flex-direction: column;
  img {
    height: 20rem;
  }
  span {
    color: #4e0eff;
  }
`;