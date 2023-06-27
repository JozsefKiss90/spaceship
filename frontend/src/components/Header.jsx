import { useNavigate } from "react-router";
import "./Header.css";
import useHandleFetchError from "./useHandleFetchError";
import { useNotificationsDispatch } from "./notifications/NotificationContext";

const Header = ({ user, setUser }) => {
  const navigate = useNavigate();
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();

  async function logout() {
    try {
      const res =fetch("/api/v1/auth/logout", {
        method: "POST",
      })
      if (res.ok) {
       setUser(null);
       navigate("/"); 
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
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
