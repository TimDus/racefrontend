import React, { useState } from "react";
import { Login } from "./components/login";
import { Signup } from "./components/signup";
import { Racer } from "./components/racer";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import "./App.css";
//import { Chat } from "./components/chat";
import { Home } from "./components/home";
import { Lobby } from "./components/lobby";

export const App = () => {
  const [userInfo, setUserInfo] = useState({
    username: "",
    password: "",
  });
  return (
    <div className="container">
      <Router>
        <Switch>
          <Route exact path="/">
            <Login userinfo={userInfo} setUserInfo={setUserInfo} />
          </Route>
          <Route exact path="/register">
            <Signup userinfo={userInfo} setUserInfo={setUserInfo} />
          </Route>
          <Route exact path="/racer" component={Racer}>
          </Route>
          <Route exact path="/home" component={Home} />
          <Route exact path="/lobby" component={Lobby} />
        </Switch>
      </Router>
    </div>
  );
};
