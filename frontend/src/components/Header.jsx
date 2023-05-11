import { useNavigate } from "react-router";
import "./Header.css";

const Header = ({ user, logout }) => {
  const navigate = useNavigate();

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
