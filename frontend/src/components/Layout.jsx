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
      console.log(token);
      setJwt(token);
      setUser(jwt_decode(token));
    } else {
      setUser(null);
      setJwt(null);
    }
  }, [jwtCookie, jwt]);

  useEffect(() => {
    if (user !== null) {
      fetch(`/api/v1/base/user/${user.userId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((res) => res.json())
        .then((data) => setStationId(data.id))
        .catch((err) => console.error(err));
    }
  }, [user, jwt]);

  function logout() {
    Cookies.remove("jwt");
    navigate("/");
  }

  console.log(user);
  console.log(stationId);

  return (
    <>
      <Header user={user} logout={logout} />
      <Outlet context={[jwt, setJwt, user, setUser, stationId]} />
      <Footer />
    </>
  );
};

export default Layout;
