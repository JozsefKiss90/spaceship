import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import "./Login&Register.css";
import { useState } from "react";

export default function Register() {
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
      await fetch("/api/v1/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      })
        .then((response) => {
          return response.json();
        })
        .then((data) => {
          Cookies.set("jwt", JSON.stringify(data.token));
          navigate("/station");
        });
    } catch (err) {
      setMessage("Username or email already used by someone else.");
    }
  }

  return (
    <div className="lrform-container">
      <form onSubmit={onSubmit}>
        <h2>Register</h2>
        <input name="email" type="email" required placeholder="Email"></input>
        <input
          name="username"
          type="text"
          required
          placeholder="Username"
        ></input>
        <input
          name="password"
          type="Password"
          required
          placeholder="password"
        ></input>
        <div>
          <input type="checkbox" required />
          <span>
            I accept the{" "}
            <a href="/terms" target="blank">
              Terms and Conditions
            </a>{" "}
            &{" "}
            <a href="/privacy" target="blank">
              Privacy Policy
            </a>
          </span>
        </div>
        <button className="button" type="Submit">
          Register
        </button>
      </form>
      {message && <div className="lrform-message">{message}</div>}
    </div>
  );
}
