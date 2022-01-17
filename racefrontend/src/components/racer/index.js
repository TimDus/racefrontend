import Stomp from "stompjs";
import "./styles.scss";

export const Racer = () => {
  let circle1;
  let circle2;

  let xpos1 = 0;
  let xpos2 = 0;

  let ypos1 = 0;
  let ypos2 = 0;

  let gameId;

  const setCords = (x1, x2, y1, y2) =>
  {
    xpos1 = x1;
    xpos2 = x2;
    ypos1 = y1;
    ypos2 = y2;
  }

  const setGameId = (id) =>
  {
    gameId = id;
  }
    
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
      const login = document.getElementById("login");
      login.classList.add("hide");

      const chatPage = document.getElementById("race-page");
      chatPage.classList.remove("hide");

      const socket = new WebSocket("ws://localhost:8081/race-websocket");

      global.stompClient = Stomp.over(socket);

      global.stompClient.connect({}, onConnected, onError);
    }
  };

  const onConnected = () => {
    global.stompClient.subscribe("/topic/temp", onMessageReceived);
    global.stompClient.send(
      "/app/race.joinLobby",
      {},
      JSON.stringify({ sender: global.username, type: "CONNECT" })
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

  const sendDirection = (event, string) => {
    event.preventDefault();

    if (global.stompClient) {
      const chatMessage = {
        sender: global.username,
        content: string,
        type: "MOVE",
      };
      console.log("chatMessage: ", chatMessage);
      global.stompClient.send(
        "/app/race.move",
        {},
        JSON.stringify(chatMessage)
      );
    }
  };

  const moveCar = (event) => {
    circle1.style.left = xpos1 + 'px';
    circle2.style.left = xpos2 + 'px';
    circle1.style.top = ypos1 + 'px';
    circle2.style.top = ypos2 + 'px';
    console.log("moved");
  }

  window.addEventListener('keydown', (e) => { 
    switch (e.key) {
        case 'ArrowLeft':
          sendDirection(e, "LEFT");
          setTimeout(500);
          break;
        case 'ArrowRight':
          sendDirection(e, "RIGHT");
          setTimeout(500);
            break;
        case 'ArrowUp':
          sendDirection(e, "UP");
          setTimeout(500);
            break;
        case 'ArrowDown':
          sendDirection(e, "DOWN");
          setTimeout(500);
            break;
    }
  });

  const assignCar = (event) => {

      circle1 =  document.getElementById("circle1");
      circle1.style.position = 'relative';

      circle2 =  document.getElementById("circle2");
      circle2.style.position = 'relative';

  }

  const messageControls = document.createElement("message-controls");
  messageControls.addEventListener("submit", sendDirection, true);

  const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);

    const messageElement = document.createElement("div");
    messageElement.className = "msg_container_send";

    if (message.type === "CONNECT") {
      messageElement.classList.add("event-message");
      console.log("message.content: ", message.content);
      console.log("message.content: ", message.content[0].xpos);
      setCords(message.content[0].xpos, message.content[1].xpos, message.content[0].ypos, message.content[1].ypos)
      assignCar();
      moveCar();
      global.stompClient.unsubscribe("/topic/temp")
    } else if (message.type === "DISCONNECT") {
      messageElement.classList.add("event-message");
      message.content = message.sender + " left!";
    }
      else if (message.type === "TRACK") {
        console.log("message.content: ", message.content);
        setCords(message.content[0].xpos, message.content[1].xpos, message.content[0].ypos, message.content[1].ypos)
        moveCar();
      } else {
      messageElement.classList.add("chat-message");

      const avatarContainer = document.createElement("div");
      avatarContainer.className = "img_cont_msg";
      const avatarElement = document.createElement("div");
      avatarElement.className = "circle user_img_msg";
      const avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style["background-color"] = getAvatarColor(message.sender);
      avatarContainer.appendChild(avatarElement);

      messageElement.style["background-color"] = getAvatarColor(message.sender);
    }

    messageElement.innerHTML = message.content;
  };

  return (
    <div>
      <div>
        <a href="/home">Back to home</a>
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
                  <div className="input-group-append">
                    <button className="fas fa-location-arrow" type="submit">
                      Join Game
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
        <div id="race-page" className="hide main">
          <div className="row justify-content-center h-100">
            <div className="col-md-8 col-xl-6">
              <div className="card-header">
                <div className="d-flex bd-highlight"></div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <main className="flexbox">
            <div id="circle1" className="circle green" ></div>
            <div id="circle2" className="circle red" ></div>
          </main>
        </div>
      </div>
    </div>
  );
};