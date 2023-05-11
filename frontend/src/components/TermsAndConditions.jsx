import "./Home.css";

export default function TermsAndConditions() {
  return (
    <div className="home-container">
      <div className="home-message">
        <h2>Terms & Conditions</h2>
        <div className="code">
          <span>{"if (condition) {"}</span>
          <span>&emsp;{"term;"}</span>
          <span>&emsp;{"term;"}</span>
          <span>&emsp;{"term;"}</span>
          <span>{"} else if (condition) {"}</span>
          <span>&emsp;{"term;"}</span>
          <span>&emsp;{"term;"}</span>
          <span>&emsp;{"term;"}</span>
          <span>&emsp;{"term;"}</span>
          <span>{"} else {"}</span>
          <span>&emsp;{"term;"}</span>
          <span>&emsp;{"term;"}</span>
          <span>{"}"}</span>
        </div>
      </div>
    </div>
  );
}
