import logo from './logo.svg';
import './App.css';
import axios from 'axios';
import {useState} from "react";

function App() {

  const [displayText, setDisplayText] = useState('Тут будет текст сервера!');

  async function sendPOSTtoBackend() {
    const data = (await axios.post('http://localhost:3001/')).data
    await setDisplayText(data.text);
  }

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <button onClick={sendPOSTtoBackend}>test</button>
        <p>{displayText}</p>
      </header>
    </div>
  );
}

export default App;
