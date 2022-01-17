import React from "react";
import "./styles.scss";
import { useHistory } from "react-router-dom";

export const Lobby = () => {
  let history = useHistory();
    
  const logout = () => {
    localStorage.clear();
    history.push("/home")
  }

  const game = () => {
    history.push("/racer")
  }

    return (
      <div>
        <nav>
          <h2>Welcome</h2> 
          <button onClick = {logout}>Log Out</button>
          <button onClick = {game}>Start game</button>
        </nav>
      </div>
    );
  
  };

  
  