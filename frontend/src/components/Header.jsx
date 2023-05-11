import './Header.css';

const Header = ({ user }) => {

    return (
        <>
            <div className="header">
                <p>MINUEND SPACESHIP GAME</p>
                {user !== null && <p>{user.sub}</p>}
            </div>
        </>
    );
};

export default Header;
