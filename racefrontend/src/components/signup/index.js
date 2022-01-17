import React from "react";
import "./styles.scss";
import { useHistory } from "react-router-dom";
import auth from "../../util/auth";

export const Signup = ({ userinfo, setUserInfo }) => {
  let history = useHistory();
  const inputChange = (e) => {
    setUserInfo({ ...userinfo, [e.target.name]: e.target.value });
  };

  const submit = (e) => {
    e.preventDefault();

    if (!userinfo.username.trim()) {
      alert("Please Enter Name");
      return;
    } else if (!userinfo.password.trim()) {
      alert("Please Enter Password");
      return;
    }

    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      mode: "cors",
      body: JSON.stringify(userinfo),
    };

    fetch("http://localhost:8080/account/register", requestOptions)
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        if (data.success) {
          auth.register(() => {
            history.push("/");
          });
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <div className="body">
      <form className="form" onSubmit={(e) => submit(e)}>
        <h1 className="login-title">Registration</h1>
        <input
          name="username"
          type="text"
          className="input"
          placeholder="Username"
          onChange={(e) => inputChange(e)}
        ></input>
        <input
          type="password"
          className="input"
          name="password"
          onChange={(e) => inputChange(e)}
          placeholder="Password"
        ></input>
        <button className="button" type="submit">
          Sign up!
        </button>
      </form>
      <div className="register">
        Already have an account? <a href="/">Log in!</a>
      </div>
    </div>
  );
};
