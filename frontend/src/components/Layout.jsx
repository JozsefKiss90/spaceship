import Footer from "./Footer";
import Header from "./Header";
import { useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import jwt_decode from "jwt-decode";
import Cookies from "js-cookie";

const Layout = () => {
  const [user, setUser] = useState(null);
  const [stationId, setStationId] = useState(null);
  const navigate = useNavigate();
  const jwtCookie = Cookies.get("jwt");

  useEffect(() => {
    if (jwtCookie) {
      setUser(jwt_decode(jwtCookie));
    }
  }, [jwtCookie]);

  useEffect(() => {
    if (user !== null) {
      fetch(`/api/v1/base/user/${user.userId}`, {})
        .then((res) => res.json())
        .then((data) => setStationId(data.id))
        .catch((err) => console.error(err));
    }
  }, [user]);

  function logout() {
    fetch("/api/v1/auth/logout", {
      method: "POST",
    }).then(() => {
      // Cookies.remove("jwt");
      setUser(null);
      setStationId(null);
      navigate("/");
    });
  }

  return (
    <>
      <Header user={user} logout={logout} />
      <Outlet context={{ user, setUser, stationId }} />
      <Footer />
    </>
  );
};

export default Layout;
