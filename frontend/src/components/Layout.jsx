import Footer from "./Footer";
import Header from "./Header";
import { useCallback, useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import jwt_decode from "jwt-decode";
import Cookies from "js-cookie";
import { NotificationProvider } from "./notifications/NotificationContext";
import Notifications from "./notifications/Notifications";

const Layout = () => {
  const navigate = useNavigate();
  const jwtCookie = Cookies.get("jwt");
  const [user, setUser] = useState(null);
  const [stationId, setStationId] = useState(null);

  useEffect(() => {
    if (jwtCookie) {
      setUser(jwt_decode(jwtCookie));
    }
  }, [jwtCookie]);

  const fetchBaseId = useCallback(async () => {
    if (user !== null) {
      try {
        const res = await fetch(`/api/v1/base/user/${user.userId}`)
        if (res.ok) {
          const data = await res.json();
          setStationId(data.id);
        } else {
          navigate("/error");
        }
      } catch (err) {
        console.error(err);
        navigate("/error");
      }
    }
  }, [user]);


  useEffect(() => {
    fetchBaseId();
  }, [fetchBaseId]);

  return (
    <NotificationProvider>
      <Header user={user} setUser={setUser} />
      <Outlet context={{ user, setUser, stationId }} />
      <Footer />
      <Notifications />
    </NotificationProvider>
  );
};

export default Layout;
