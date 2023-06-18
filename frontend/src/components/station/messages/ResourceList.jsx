export default function ResourceList({message, resources}) {
  return (
    <>
      <div>{message}</div>
      {Object.keys(resources).map((key) => {
        return (
          <div className="resource-row" key={key}>
            <img
              style={{ width: "25px", height: "25px" }}
              src={"/" + key.toLowerCase() + ".png"}
              alt={key}
            />
            <div style={{ marginLeft: "5px" }}>
              {key} : {resources[key]}
            </div>
          </div>
        );
      })}
    </>
  );
}
