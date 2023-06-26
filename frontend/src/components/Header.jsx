import { useNavigate } from "react-router";
import "./Header.css";

const Header = ({ user, setUser }) => {
  const navigate = useNavigate();

  function logout() {
    fetch("/api/v1/auth/logout", {
      method: "POST",
    }).then(() => {
      setUser(null);
      navigate("/");
    });
  }

  return (
    <>
      <div className="header">
        <p onClick={() => navigate("/")}>MINUEND SPACESHIP GAME</p>
        {user === null && (
          <span className="push" onClick={() => navigate("/login")}>
            Login
          </span>
        )}
        {user !== null && (
          <span className="push" onClick={() => navigate("/station")}>
            {user.sub}
          </span>
        )}
        {user !== null && <span onClick={logout}>Log out</span>}
      </div>
    </>
  );
};

export default Header;
