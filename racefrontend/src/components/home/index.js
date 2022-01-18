import React from "react";
import "./styles.scss";
import { useHistory } from "react-router-dom";

export const Home = () => {
  let history = useHistory();
    
  const logout = () => {
    localStorage.clear();
    history.push("/")
  }

  const lobby = () => {
    history.push("/lobby")
  }

    return (
      <div>
        <nav>
          <h2>Welcome</h2> 
          <button onClick = {logout}>Log Out</button>
          <button onClick = {lobby}>Lobby</button>
        </nav>
      </div>
    );
  
  };
  