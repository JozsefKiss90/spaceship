import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import "./Login&Register.css";
import { useState } from "react";

const Login = () => {
  const navigate = useNavigate();
  const [message, setMessage] = useState(null);

  const onSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const entries = [...formData.entries()];
    const credentials = entries.reduce((acc, entry) => {
      const [k, v] = entry;
      acc[k] = v;
      return acc;
    }, {});
    handleLogin(credentials);
  };

  async function handleLogin(formData) {
    try {
      await fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      })
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          //   setJwt(data.token);
          Cookies.set("jwt", JSON.stringify(data.token));
          navigate("/station");
        });
    } catch (err) {
      setMessage("Incorrect username or password.");
    }
  }

  return (
    <div className="lrform-container">
      <form onSubmit={onSubmit}>
        <h2>Login</h2>
        <input
          name="username"
          type="text"
          required
          placeholder="Username"
        ></input>
        <input
          name="password"
          type="password"
          required
          placeholder="Password"
        ></input>
        <button className="button" type="submit">
          Login
        </button>
      </form>
      {message && <div className="lrform-message">{message}</div>}
    </div>
  );
};

export default Login;
