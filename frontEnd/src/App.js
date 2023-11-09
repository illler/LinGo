import React from 'react';
import {BrowserRouter, BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Register from "./Pages/Register";
import Chat from "./Pages/Chat";
import Login from "./Pages/Login";


const App = () => {
    return (
           <BrowserRouter>
               <Routes>
                   <Route path="/register" element={<Register />}/>
                   <Route path="/login" element={<Login />}/>
                   <Route path="/" element={<Chat />}/>
               </Routes>
           </BrowserRouter>
    );
};

export default App;