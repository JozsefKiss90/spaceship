import "./Home.css";

export default function Home() {
  return (
    <div className="home-container">
      <div className="home-message">
        <h2>Welcome, stranger!</h2>
        <span>
          Are you ready to be the best Commander in the Minuend Galaxy?
        </span>
        <br />
        <span>Start with a basic space station with one miner ship.</span>
        <span>Send ships on missions to gather resources.</span>
        <span>Spend your resources on storage and hangar upgrades.</span>
        <span>
          Add more ships and upgrade their parts to improve efficiency.
        </span>
        <span>
          Get on top of the Minuend Galaxy Space Station Commander Leaderboard!
        </span>
        <br />
        <span>
          <a href="/register">Register</a> now to join the fun!
        </span>
        <br />
        <span>
          If you already are a Commander, <a href="/login">log in.</a>
        </span>
        <br />
        <span>
          If you have issues logging in, click <a href="/issues">here.</a>
        </span>
        <br />
        <span>
          If you are bored, read our <a href="/terms">Terms and Conditions</a>{" "}
          and <a href="/privacy">Privacy Policy.</a>
        </span>
      </div>
    </div>
  );
}
