import Footer from './Footer';
import Header from './Header';
import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import jwt_decode from 'jwt-decode';

const Layout = () => {
    const [jwt, setJwt] = useState(null);
    const [user, setUser] = useState(null);
    const [stationId, setStationId] = useState(null);

    useEffect(() => {
        if (jwt === null) {
            setUser(null);
        } else {
            setUser(jwt_decode(jwt));
        }
    }, [jwt]);

    useEffect(() => {
        if (user !== null) {
            fetch(`/base/user/${user.userId}`, {
                "headers": {
                    "Authorization": `Bearer ${jwt}`
                }
            })
                .then((res) => res.json())
                .then((data) => setStationId(data.id))
                .catch((err) => console.error(err));
        }
    }, [user, jwt]);

    console.log(user);
    console.log(stationId);

    return (
        <>
            <Header user={user} />
            <Outlet context={[jwt, setJwt, user, setUser, stationId]} />
            <Footer />
        </>
    );
};

export default Layout;