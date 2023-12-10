import React, { useState, useEffect } from "react";
import axios from "axios";
import styled from "styled-components";
import { useNavigate, Link } from "react-router-dom";
import Logo from "../assets/logo.svg";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import API from "../Actions/API";

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
                <div className="brand">
                    <img src={Logo} alt="logo" />
                    <h1>LinGo</h1>
                </div>
                <form onSubmit={handleSubmitLogin}>
                    <input
                        type="text"
                        placeholder="Email"
                        name="email"
                        onChange={(e) => handleChange(e)}
                        min="3"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        name="password"
                        onChange={(e) => handleChange(e)}
                    />
                    <button type="submit">Log In</button>
                </form>
                <form onSubmit={handleSubmitRegister}>
                    <input
                        type="text"
                        placeholder="First Name"
                        name="firstname"
                        onChange={(e) => handleChange(e)}
                    />
                    <input
                        type="text"
                        placeholder="Last Name"
                        name="lastname"
                        onChange={(e) => handleChange(e)}
                    />
                    <input
                        type="email"
                        placeholder="Email"
                        name="email"
                        onChange={(e) => handleChange(e)}
                    />
                    <input
                        type="text"
                        placeholder="Lang"
                        name="lang"
                        onChange={(e) => handleChange(e)}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        name="password"
                        onChange={(e) => handleChange(e)}
                    />
                    <input
                        type="password"
                        placeholder="Confirm Password"
                        name="confirmPassword"
                        onChange={(e) => handleChange(e)}
                    />
                    <button type="submit">Create User</button>
                </form>
            </FormContainer>
            <ToastContainer />
        </MainContainer>
    );
}


const MainContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  background-color: #131324;
  color: white;
`;


const FormContainer = styled.div`
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 1rem;
  align-items: center;
  background-color: #131324;
  .brand {
    display: flex;
    align-items: center;
    gap: 1rem;
    justify-content: center;
    img {
      height: 5rem;
    }
    h1 {
      color: white;
      text-transform: uppercase;
    }
  }

  form {
    display: flex;
    flex-direction: column;
    gap: 2rem;
    background-color: #00000076;
    border-radius: 2rem;
    padding: 3rem 5rem;
  }
  input {
    background-color: transparent;
    padding: 1rem;
    border: 0.1rem solid #4e0eff;
    border-radius: 0.4rem;
    color: white;
    width: 100%;
    font-size: 1rem;
    &:focus {
      border: 0.1rem solid #997af0;
      outline: none;
    }
  }
  button {
    background-color: #4e0eff;
    color: white;
    padding: 1rem 2rem;
    border: none;
    font-weight: bold;
    cursor: pointer;
    border-radius: 0.4rem;
    font-size: 1rem;
    text-transform: uppercase;
    &:hover {
      background-color: #4e0eff;
    }
  }
  span {
    color: white;
    text-transform: uppercase;
    a {
      color: #4e0eff;
      text-decoration: none;
      font-weight: bold;
    }
  }
`;

