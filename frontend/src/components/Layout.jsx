import Footer from './Footer';
import Header from './Header';
import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import jwt_decode from 'jwt-decode';

const Layout = () => {
    const [jwt, setJwt] = useState(null);
    const [user, setUser] = useState(null);

    useEffect(() => {
        if (jwt === null) {
            setUser(null);
        } else {
            setUser(jwt_decode(jwt));
        }
    }, [jwt]);

    return (
        <>
            <Header user={user} />
            <Outlet context={[jwt, setJwt, user, setUser]} />
            <Footer />
        </>
    );
};

export default Layout;