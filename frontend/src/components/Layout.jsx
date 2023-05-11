import Footer from './Footer';
import Header from './Header';
import {useState} from "react";
import {Outlet} from "react-router-dom";
const Layout = () => {
    const [user, setUser] = useState(null);

    return (
        <>
            <Header />
            <Outlet context={[user, setUser]}/>
            <Footer />
        </>
    );
};

export default Layout;