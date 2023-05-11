import "./Header.css";

const Header = ({ user, logout }) => {
  return (
    <>
      <div className="header">
        <p>MINUEND SPACESHIP GAME</p>
        {user !== null && <p>{user.sub}</p>}
        {user !== null && <button onClick={logout}>Log out</button>}
      </div>
    </>
  );
};

export default Header;
