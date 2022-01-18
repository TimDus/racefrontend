import React from "react";
import "./styles.scss";
import { useHistory } from "react-router-dom";
import Stomp from "stompjs";
import { useState } from "react";

export const Lobby = () => {
  let history = useHistory();

  let lobbyId;

  let selectedId

  const setLobbyId = (id) => {
    lobbyId = id;
  }

  let [data, setData] = useState([{id: 0}]);

  const transferData = (lobbydata) => {
    setData(lobbydata);
  };
  
  const home = () => {
    history.push("/home")
  }
  
  const game = () => {
    global.stompClient.unsubscribe("/topic/temp")
    history.push("/racer")
  }

  const handleClick = (id) => {selectedId = id;
  console.log(selectedId)};

  const parseJwt = (token) => {
    if (!token) {
      return;
    }
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace("-", "+").replace("_", "/");
    return JSON.parse(window.atob(base64));
  };

  const connect = (e) => {
    e.preventDefault();

    let token = localStorage.getItem("jwtToken");
    let parsedToken = parseJwt(token);
    global.username = parsedToken.sub;

    if (global.username) {
      const connect = document.getElementById("connect");
      connect.classList.add("hide");

      const lobbysocket = new WebSocket("ws://localhost:8079/lobby-websocket");

      console.log("message.content: ", lobbysocket);
      global.stompClient = Stomp.over(lobbysocket);

      global.stompClient.connect({}, onConnected, onError);
    }
  };

  const create = (e) => {
    e.preventDefault();

    global.stompClient.send(
      "/app/lobby.createLobby",
      {},
      JSON.stringify({ sender: global.username, type: "CONNECT" })
    );
  };

  const refresh = (e) => {
    e.preventDefault();

    global.stompClient.send(
      "/app/lobby.refreshLobby",
      {},
      JSON.stringify({ sender: global.username, type: "REFRESH" })
    );
  };

  const join = (e) => {
    e.preventDefault();

    global.stompClient.send(
      "/app/lobby.joinLobby",
      {},
      JSON.stringify({ sender: global.username, type: "JOIN", content: selectedId })
    );
  };

  const onConnected = () => {
    global.stompClient.subscribe("/topic/lobby", onMessageReceived);
    const status = document.getElementById("status");
    status.className = "hide";
  };

  const onError = (error) => {
    const status = document.querySelector("#status");
    status.innerHTML =
      "Could not find the connection you were looking for. Move along. Or, Refresh the page!";
    status.style.color = "red";
  };

  const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    const messageElement = document.createElement("div");
    messageElement.className = "msg_container_send";

    if (message.type === "CONNECT") {
      messageElement.classList.add("event-message");
    } else if (message.type === "DISCONNECT") {
      messageElement.classList.add("event-message");
      message.content = message.sender + " left!";
    }
    else if (message.type === "CREATE") 
    {
      setLobbyId(message.content)
      console.log(lobbyId)
    }
    else if (message.type === "REFRESH")
    {
      transferData(message.content);
      console.log(message.content)
      console.log(data)
      const lobbiesdata = document.getElementById("lobbies");
      lobbiesdata.classList.remove("hide");
    }
    else {}

    messageElement.innerHTML = message.content;
  };

    return (
      <div>
        <nav>
          <h2>Welcome</h2> 
          <button onClick = {home}>Home</button>
          <button onClick = {game}>Start game</button>
          <div id="status" className="login"></div>
          <div className="main row justify-content-center h-100">
              <form
                id="connect"
                name="login-form"
                onSubmit={(e) => connect(e)}
              >
                <div className="input-group">
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      Connect to Lobby system
                    </button>
                  </div>
                </div>
              </form>
              <form
                id="after"
                name="login-form"
                onSubmit={(e) => create(e)}
              >
                <div className="input-group">
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      Create lobby
                    </button>
                  </div>
                </div>
              </form>
              <form
                id="after"
                name="login-form"
                onSubmit={(e) => join(e)}
              >
                <div className="input-group">
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      Join lobby
                    </button>
                  </div>
                </div>
              </form>
              <form
                id="after"
                name="login-form"
                onSubmit={(e) => refresh(e)}
              >
                <div className="input-group">
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      Refresh lobbies 
                    </button>
                  </div>
                </div>
              </form>
              <div id="lobbies" className="hide">
                <table className="no-border">
                  <tbody>
                    <tr className="lobbyrow" >open lobbies</tr>
                    {data.map(number => <tr className="lobbyrow" onClick={() => {handleClick(number.id);}}>{number.id}</tr>)}
                  </tbody>
                </table>
              </div>
            </div>
            <div>
        </div>
        </nav>
      </div>
    );
  
  };

  
  