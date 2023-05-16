import Footer from "./Footer";
import Header from "./Header";
import { useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import jwt_decode from "jwt-decode";
import Cookies from "js-cookie";

const Layout = () => {
  const [jwt, setJwt] = useState(null);
  const [user, setUser] = useState(null);
  const [stationId, setStationId] = useState(null);
  const jwtCookie = Cookies.get("jwt");
  const navigate = useNavigate();

  useEffect(() => {
    if (jwtCookie) {
      const token = JSON.parse(jwtCookie);
      setJwt(token);
      setUser(jwt_decode(token));
    } else {

    }
  }, [jwtCookie, jwt]);

  useEffect(() => {
    if (user !== null) {
      fetch(`/base/user/${user.userId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then(res => res.json())
        .then(data => setStationId(data.id))
        .catch(err => console.error(err));
    }
  }, [user, jwt]);

  function logout() {
    Cookies.remove("jwt");
    // setTriggerLogout(!triggerLogout);
    setUser(null);
    setJwt(null);
    setStationId(null);
    navigate("/");
  }

  return (
    <>
      <Header user={user} logout={logout} />
      <Outlet context={{"jwt":jwt, "setJwt":setJwt, "user":user, "setUser":setUser, "stationId":stationId}} />
      <Footer />
    </>
  );
};

export default Layout;
