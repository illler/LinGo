import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegistrationForm from './Pages/RegistrationForm';
import AuthorizationForm from './Pages/AuthorizationForm';
import HomePage from "./Pages/HomePage";
import DemoPage from "./Pages/DemoPage";
import ChatRoom from "./Pages/ChatRoom";


const App = () => {
    return (
            <Router>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/demo" element={<DemoPage />} />
                    <Route path="/registration" element={<RegistrationForm />} />
                    <Route path="/authorization" element={<AuthorizationForm />} />
                    <Route path="/chat" element={<ChatRoom />} />
                </Routes>
            </Router>
    );
};

export default App;