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
      const res = await fetch("/api/v1/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });
      if (res.ok) {
        const data = await res.json();
        if (data) {
          navigate("/station");
        }
      } else {
        let message;
        try {
          const data = await res.json();
          if (data.detail) {
            message = data.detail;
          } else throw new Error();
        } catch (err) {
          message = res.statusText;
        }

        throw new Error(message);
      }
    } catch (err) {
      setMessage(err.message);
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
            <a className="clickable" href="/terms" target="blank">
              Terms and Conditions
            </a>{" "}
            &{" "}
            <br />
            <a className="clickable" href="/privacy" target="blank">
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
