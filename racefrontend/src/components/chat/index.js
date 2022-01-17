import Stomp from "stompjs";
import "./styles.scss";
import React from "react";

export const Chat = (
  { userinfo, setUserInfo },
  { boardData, setBoardData }
) => {
  const inputChange = (e) => {
    setUserInfo({ ...userinfo, [e.target.name]: e.target.value });
  };
  let stompClient;
  let username;
  const connect = (e) => {
    e.preventDefault();

    username = document.getElementById("username");

    if (username) {
      const login = document.getElementById("login");
      login.classList.add("hide");

      const chatPage = document.getElementById("chat-page");
      chatPage.classList.remove("hide");

      //ToDo:

      const socket = new WebSocket("ws://localhost:8080/checkers-websocket");

      stompClient = Stomp.over(socket);
      stompClient.connect({}, onConnected, onError);
    }
  };

  const onConnected = () => {
    stompClient.subscribe("/topic/public", onMessageReceived);
    stompClient.send(
      "/app/race.newUser",
      {},
      JSON.stringify({ sender: username.value, type: "CONNECT" })
    );
    const status = document.getElementById("status");
    status.className = "hide";
  };

  const onError = (error) => {
    const status = document.querySelector("#status");
    status.innerHTML =
      "Could not find the connection you were looking for. Move along. Or, Refresh the page!";
    status.style.color = "red";
  };

  const sendMessage = (event) => {
    event.preventDefault();

    const messageInput = document.getElementById("message");
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
      const checkersMessage = {
        sender: username.value,
        content: messageInput.value,
        type: "CHAT",
      };
      stompClient.send(
        "/app/checkers.send",
        {},
        JSON.stringify(checkersMessage)
      );
      messageInput.value = "";
    }
  };

  const hashCode = (str) => {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    return hash;
  };

  const getAvatarColor = (messageSender) => {
    const colours = ["#2196F3", "#32c787", "#1BC6B4", "#A1B4C4"];
    const index = Math.abs(hashCode(messageSender) % colours.length);
    return colours[index];
  };

  const loginForm = document.createElement("login-form");
  loginForm.addEventListener("submit", connect, true);
  const messageControls = document.createElement("message-controls");
  messageControls.addEventListener("submit", sendMessage, true);

  const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);

    const chatCard = document.createElement("div");
    chatCard.className = "card-body";

    const flexBox = document.createElement("div");
    flexBox.className = "d-flex justify-content-end mb-4";
    chatCard.appendChild(flexBox);

    const messageElement = document.createElement("div");
    messageElement.className = "msg_container_send";

    flexBox.appendChild(messageElement);

    if (message.type === "CONNECT") {
      messageElement.classList.add("event-message");
      setBoardData(message.content);
      //message.content = message.sender + " connected!";
    } else if (message.type === "DISCONNECT") {
      messageElement.classList.add("event-message");
      message.content = message.sender + " left!";
    } else {
      messageElement.classList.add("checkers-message");

      const avatarContainer = document.createElement("div");
      avatarContainer.className = "img_cont_msg";
      const avatarElement = document.createElement("div");
      avatarElement.className = "circle user_img_msg";
      const avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style["background-color"] = getAvatarColor(message.sender);
      avatarContainer.appendChild(avatarElement);

      messageElement.style["background-color"] = getAvatarColor(message.sender);

      flexBox.appendChild(avatarContainer);

      const time = document.createElement("span");
      time.className = "msg_time_send";
      time.innerHTML = message.time;
      messageElement.appendChild(time);
    }

    messageElement.innerHTML = message.content;

    const chat = document.querySelector("#chat");
    chat.appendChild(flexBox);
    chat.scrollTop = chat.scrollHeight;
  };

  return (
    <div>
      <div>
        <title>App</title>
        <div className="container-fluid h-100">
          <div id="status" className="login"></div>
          <div id="login">
            <div className="main row justify-content-center h-100">
              <form
                id="login-form"
                name="login-form"
                onSubmit={(e) => connect(e)}
              >
                <div className="input-group">
                  <input
                    id="username"
                    type="text"
                    className="form-control all-input"
                    autoComplete="off"
                    placeholder="Username"
                    onChange={(e) => inputChange(e)}
                  />
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      X
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div id="chat-page" className="hide main">
            <div className="row justify-content-center h-100">
              <div className="col-md-8 col-xl-6">
                <div className="card">
                  <div className="card-header">
                    <div className="d-flex bd-highlight">
                      <div className="chat-summary">
                        <span>Web Sockets with Spring Boot Chat</span>
                      </div>
                    </div>
                  </div>

                  <div className="card-body">
                    <div id="chat"></div>
                  </div>

                  <form
                    id="message-controls"
                    name="message-controls"
                    className="card-footer"
                    onSubmit={(e) => sendMessage(e)}
                  >
                    <div className="input-group">
                      <textarea
                        id="message"
                        className="form-control all-input"
                        placeholder="Type your message..."
                      ></textarea>
                      <div className="input-group-append">
                        <button className="fas fa-location-arrow" type="submit">
                          X
                        </button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
