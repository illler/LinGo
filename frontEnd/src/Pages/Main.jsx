import React, { useState, useEffect } from "react";
import axios from "axios";
import styled from "styled-components";
import { useNavigate, Link } from "react-router-dom";
import Logo from "../assets/logo.svg";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import API from "../Actions/API";

import Header from "../assets/header.svg";
import SignIn from "../assets/user.svg";
import InfoCard from "../assets/infoContainer.png";
import PromoCard from "../assets/promoContainer.png";
import LoginCard from "../assets/loginContainer.png";


export default function Main() {

    localStorage.removeItem('authToken');


    const navigate = useNavigate();
    const [values, setValues] = useState({
        email: "",
        password: "",
        firstname: "",
        lastname: "",
        lang: "",
        confirmPassword: "",
    });
    const toastOptions = {
        position: "bottom-right",
        autoClose: 8000,
        pauseOnHover: true,
        draggable: true,
        theme: "dark",
    };
    useEffect(() => {
        if (localStorage.getItem("authToken")) {
            navigate("/");
        }
    }, []);

    const handleChange = (event) => {
        setValues({ ...values, [event.target.name]: event.target.value });
    };

    const validateForm = () => {
        const { email, password } = values;
        if (email === "" || password === "") {
            toast.error("Email and Password are required.", toastOptions);
            return false;
        }
        return true;
    };

    const handleValidation = () => {
        const { password, confirmPassword, firstname, lastname, email } = values;
        if (password !== confirmPassword) {
            toast.error(
                "Password and Confirm Password should be the same.",
                toastOptions
            );
            return false;
        } else if (firstname.length < 3 || lastname.length < 3) {
            toast.error(
                "First name and Last name should be greater than 3 characters.",
                toastOptions
            );
            return false;
        } else if (password.length < 3) {
            toast.error(
                "Password should be equal or greater than 8 characters.",
                toastOptions
            );
            return false;
        } else if (email === "") {
            toast.error("Email is required.", toastOptions);
            return false;
        }
        return true;
    };

    const handleSubmitLogin = async (event) => {
        event.preventDefault();
        if (validateForm()) {
            const { email, password } = values;
            try {
                const { data } = await axios.post(API.USER.AUTH.LOGIN, {
                    email,
                    password,
                });
                if (data.token !== null) {
                    localStorage.setItem("authToken", data.token);
                    navigate("/");
                }
            } catch (error) {
                toast.error("Invalid credentials. Please try again.", toastOptions);
            }
        }
    };

    const handleSubmitRegister = async (event) => {
        event.preventDefault();
        if (handleValidation()) {
            const { password, firstname, lastname, email, lang } = values;
            try {
                const { data } = await axios.post(API.USER.AUTH.REGISTRATION, {
                    firstname,
                    lastname,
                    email,
                    lang,
                    password,
                    role: "USER",
                });
                if (data.token !== null) {
                    localStorage.setItem("authToken", data.token);
                    navigate("/");
                }
            } catch (error) {
                toast.error("Registration failed. Please try again.", toastOptions);
            }
        }
    };



    return (
        <MainContainer>
            <FormContainer>
                <div className={"wrapper"}>
                    <div className={"mainContainer"}>
                        <header className={"header"}>
                            <div className={"nav"}>
                                <p>о нас</p>
                                <p>регистрация</p>
                                <img src={SignIn} alt={"sign in"} className={"signIn-icon"}/>
                            </div>
                            <img src={Header} alt={"header"} className={"headerImg"}/>
                        </header>
                        <div className={"mainContent"}>
                            <div className={"col"}>
                                    <div className={"row infoCard"}>
                                        <img src={InfoCard} alt={"infoCard"} className={"InfoCard-icon"}/>
                                    </div>
                                    <div className={"row promoCards"}>
                                        <div className={"col"}>
                                            <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon "}/>
                                        </div>
                                        <div className={"col"}>
                                            <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon "}/>
                                        </div>
                                    </div>
                            </div>
                            <div className={"col"}>
                                <div className={"row"}>
                                    <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon "}/>
                                </div>
                                <div className={"row"}>
                                    <div className={"col"}>
                                        <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon "}/>
                                    </div>
                                </div>
                            </div>

                            {/*<div className={"row"}>*/}
                            {/*    <div className={"col infoCard"}>*/}
                            {/*        <img src={InfoCard} alt={"infoCard"} className={"InfoCard-icon"}/>*/}
                            {/*    </div>*/}
                            {/*    <div className={"col"}>*/}
                            {/*        <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon topPromo"}/>*/}
                            {/*    </div>*/}
                            {/*</div>*/}

                            {/*<div className={"row-2"}>*/}
                            {/*    <div className={"col-2"} >*/}
                            {/*        <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon"}/>*/}
                            {/*    </div>*/}
                            {/*    <div className={"col-2"} >*/}
                            {/*        <img src={PromoCard} alt={"promoCard"} className={"PromoCard-icon"}/>*/}
                            {/*    </div>*/}
                            {/*    <div className={"col-2"} >*/}
                            {/*        <img src={LoginCard} alt={"loginCard"} className={"PromoCard-icon"}/>*/}
                            {/*    </div>*/}
                            {/*</div>*/}
                        </div>
                    </div>
                </div>
                {/*<form onSubmit={handleSubmitLogin}>*/}
                {/*    <input*/}
                {/*        type="text"*/}
                {/*        placeholder="Email"*/}
                {/*        name="email"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*        min="3"*/}
                {/*    />*/}
                {/*    <input*/}
                {/*        type="password"*/}
                {/*        placeholder="Password"*/}
                {/*        name="password"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <button type="submit">Log In</button>*/}
                {/*</form>*/}
                {/*<form onSubmit={handleSubmitRegister}>*/}
                {/*    <input*/}
                {/*        type="text"*/}
                {/*        placeholder="First Name"*/}
                {/*        name="firstname"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <input*/}
                {/*        type="text"*/}
                {/*        placeholder="Last Name"*/}
                {/*        name="lastname"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <input*/}
                {/*        type="email"*/}
                {/*        placeholder="Email"*/}
                {/*        name="email"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <input*/}
                {/*        type="text"*/}
                {/*        placeholder="Lang"*/}
                {/*        name="lang"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <input*/}
                {/*        type="password"*/}
                {/*        placeholder="Password"*/}
                {/*        name="password"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <input*/}
                {/*        type="password"*/}
                {/*        placeholder="Confirm Password"*/}
                {/*        name="confirmPassword"*/}
                {/*        onChange={(e) => handleChange(e)}*/}
                {/*    />*/}
                {/*    <button type="submit">Create User</button>*/}
                {/*</form>*/}
            </FormContainer>
            <ToastContainer />
        </MainContainer>
    );
}


const MainContainer = styled.div`
  display: block;
  overflow-x: auto;
  
  .wrapper {
    display: flex;
    justify-content: center;
    
    width: 130.73vw;
    height: 100vh;

    background: #131313;
  }
  
  .mainContainer {
    display: flex;
    margin-top: 3.14vh;

    width: 120.95vw;
    height: 94vh;
    
    border-radius: 56px;
    background: #F1F1F1;
  }
  
  .header {
    position: absolute;
    top: 0;
    left: 62.674vw;
  }
  
  .headerImg {
    height: 11.9vh;
  }
  
  .nav {
    display: flex;
    gap: 20px;
    
    position: absolute;
    top: 4vh;
    
    margin-left: 4.225vw;
  }
  
  .nav p {
    color: #F1F1F1;
    text-align: center;
    font-family: Stolzl, sans-serif;
    font-size: 1.2vw;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    
    margin-top: 0.834vh;
  }
  
  .signIn-icon {
    width: 40px;
  }
  
  .row {
    display: flex;
    flex-direction: row;

    width: 37vw;
  }
  
  .row-2 {
    display: flex;
    flex-direction: row;
    gap: 5vw;
    margin-top: 6vh;
  }
  
  .topPromo {
    margin-top: 5vh;
    
  }
  
  .mainContent {
    display: flex;
    flex-direction: row;
    margin-top: 8vh;
    margin-left: 6.42vw;
  }
  
  .InfoCard-icon {
    height: 40vh;
    width: 37vw;
    
  }
  
  .PromoCard-icon {
    height: 35vh;
    
  }
  
  .promoCards {
    margin-top: 6vh;
    
    display: flex;
    justify-content: space-between;
  }
  

`;



const FormContainer = styled.div`
  display: block;
`;


