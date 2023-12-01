import React, {useState} from "react"
import styled from "styled-components"
import {Link, useNavigate} from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import Logo from "../assets/logo.svg"
import axios from "axios";
import API from "../Actions/API";

export default function Register(){
    const navigate = useNavigate();
    const [values, setValues] = useState({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        confirmPassword: "",
    });
    localStorage.removeItem('authToken');

    const toastOptions = {
        position: "bottom-right",
        autoClose: 8000,
        pauseOnHover: true,
        draggable: true,
        theme: "dark",
    };

    const handleSubmit = async (event)=>{
        event.preventDefault()
        if(handleValidation()){
            const {password, firstname, lastname, email, lang} = values
            const {data} = await axios.post(API.USER.AUTH.REGISTRATION,
                {
                    firstname,
                    lastname,
                    email,
                    lang,
                    password,
                    role:"USER",
                });

            if (data.token !== null) {
                console.log(data.token)
                localStorage.setItem('authToken', data.token);
                navigate("/");
            }
        }
    }
    const handleChange = (event) => {
        setValues({ ...values, [event.target.name]: event.target.value });
    };

    const handleValidation = () => {
        const { password, confirmPassword, firstname, lastname, email } = values;
        if (password !== confirmPassword) {
            toast.error(
                "Password and confirm password should be same.",
                toastOptions
            );
            return false;

            // #TODO: исправить проверку на firstname
        } else if (firstname.length < 3) {
            toast.error(
                "firstname, lastname should be greater than 3 characters.",
                toastOptions
            );
            return false;
            // #TODO исправить проверку на пароль
        } else if (password.length < 2) {
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


    return(
        <>
            <FormContainer>
                <form action="" onSubmit={(event) => handleSubmit(event)}>
                    <div className="brand">
                        <img src={Logo} alt="logo" />
                        <h1>LinGo</h1>
                    </div>
                    <input
                        type="text"
                        placeholder="FirstName"
                        name="firstname"
                        onChange={(e) => handleChange(e)}
                    />
                    <input
                        type="text"
                        placeholder="LastName"
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
                    <span>
                        Already have an account ? <Link to="/login">Login.</Link>
                    </span>
                </form>
            </FormContainer>
        </>
    )
}

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
