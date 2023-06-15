export default function ResourceList({message, cost}) {
  return (
    <>
      <div>{message}</div>
      {Object.keys(cost).map((key) => {
        return (
          <div className="message-row" key={key}>
            <img
              style={{ width: "25px", height: "25px" }}
              src={"/" + key.toLowerCase() + ".png"}
              alt={key}
            />
            <p style={{ marginLeft: "5px" }}>
              {key} : {cost[key]}
            </p>
          </div>
        );
      })}
    </>
  );
}
