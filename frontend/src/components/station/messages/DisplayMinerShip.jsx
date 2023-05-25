import "./DisplayMinerShip.css";
import { ResourceNeeded } from "./ResourceNeeded";
import { useEffect, useState } from "react";
import { useOutletContext, useParams } from "react-router-dom";
import { useMessageDispatchContext } from "../MessageContext";
import { ShipName } from "./ShipName";
import { ShipColor } from "./ShipColor";
import { ShipStatus } from "./ShipStatus";

export default function DisplayMinerShip({ message, checkStorage }) {
  const dispatch = useMessageDispatchContext();
  const { stationId } = useOutletContext();
  const { id } = useParams();
  const [ship, setShip] = useState(null);
  const [resource, setResource] = useState(null);
  const [storage, setStorage] = useState(null);
  const [displayResource, setDisplayResource] = useState(false);
  const [resourceMessage, setResourceMessage] = useState(null);
  const [show, setShow] = useState(false);
  const [part, setPart] = useState(null);

  console.log(id);

  useEffect(() => {
    fetch(`/api/v1/ship/miner/${id}`)
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setShip(data);});
  }, [id]);

  async function getShipPartUpgradeCost(part) {
    await fetch(`/api/v1/ship/miner/${ship.id}/upgrade?part=${part}`)
      .then((res) => res.json())
      .then((data) => setResource(data))
      .catch((err) => console.error(err));
  }

  function getStorage() {
    fetch(`/api/v1/base/${stationId}/storage/resources`)
      .then((res) => res.json())
      .then((data) => setStorage(data))
      .catch((err) => console.error(err));
  }

  async function onClick(part) {
    await getStorage();
    await getShipPartUpgradeCost(part);
    setPart(part);
    setDisplayResource(!displayResource);
  }

  useEffect(() => {
      if (part && storage && resource) {
          setResourceMessage({
              part: part,
              type: part.toLowerCase() + " upgrade",
              data: resource,
              storage: storage,
              id: ship.id,
          });
          setShow(true);
      }
      setPart(null);
      setResource(null);
      setStorage(null);
  }, [displayResource]);

  if (ship == null) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <div
        className="container"
        style={{ display: "flex", flexFlow: "column" }}
      >
        <ShipName ship={ship} />
        <div className="ship-type" style={{ fontSize: "13px", height: "15px" }}>
          miner ship
        </div>
         <ShipColor ship={ship} />
        <ShipStatus ship={ship} />
        <div className="ship-shield row">
          <div>
            Shield | lvl: {ship.shieldLevel} | {ship.shieldEnergy} /{" "}
            {ship.maxShieldEnergy}
          </div>
          <div className="button" onClick={() => onClick("SHIELD")}>
            Upgrade
          </div>
        </div>
        <div className="ship-drill row">
          <div>
            Drill | lvl: {ship.drillLevel} | resource/hour:{" "}
            {ship.drillEfficiency}
          </div>
          <div className="button" onClick={() => onClick("DRILL")}>
            {" "}
            Upgrade
          </div>
        </div>
        <div className="ship-engine row">
          <div>
            Engine | lvl: {ship.engineLevel} | lightyear/hour: {ship.maxSpeed}
          </div>
          <div className="button" onClick={() => onClick("ENGINE")}>
            Upgrade
          </div>
        </div>
        <div className="ship-storage row">
          <div>
            Storage | lvl: {ship.storageLevel} |{" "}
            {ship.amountOfCurrentlyStoredItems} / {ship.maxStorageCapacity}
          </div>
          <div className="button" onClick={() => onClick("STORAGE")}>
            Upgrade
          </div>
        </div>
        <div
          className="ship-storage-contents"
          style={{
            display: "flex",
            flexFlow: "column",
            alignItems: "flex-start",
            height: "min-content",
          }}
        >
          {ship.resources.length > 0 ? (
            ship.resources.map((resource) => {
              console.log(resource);
              return (
                <div className="resource-row" key={resource.resourceType}>
                  <img
                    style={{ width: "25px", height: "25px" }}
                    src={"/" + resource.resourceType.toLowerCase() + ".png"}
                    alt={resource.resourceType}
                  />
                  <div
                    style={{ marginLeft: "5px" }}
                    key={resource.resourceType}
                  >
                    {resource.resourceType}: {resource.quantity}
                  </div>
                </div>
              );
            })
          ) : (
            <div>Ship storage is empty</div>
          )}
        </div>
        <div style={{ height: "min-content", paddingTop: "15px" }}>
          {show ? (
            <ResourceNeeded
              message={resourceMessage}
              checkStorage={checkStorage}
            />
          ) : (
            <></>
          )}
        </div>
      </div>
      <div className="ship-img">
        <img
        alt="spaceship"
          src="/ship_five.png"
          style={{
            width: "128px",
            height: "128px",
            padding: "60px",
            justifySelf: "center",
            alignSelf: "center",
          }}
        />
      </div>
    </>
  );
}
