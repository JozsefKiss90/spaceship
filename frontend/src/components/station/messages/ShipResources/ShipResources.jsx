import { useEffect, useState } from "react";
import ResourceList from "../ResourceList";
import MoveResources from "./MoveResources";
import { useStorageDispatchContext } from "../../StorageContext";
import "./ShipResources.css";

export default function ShipResources({ ship, setShip }) {
  const [moveMode, setMoveMode] = useState(false);
  const storageSetter = useStorageDispatchContext();

  useEffect(() => {
    setMoveMode(false);
  }, [ship]);

  function applyMove(moveAmount) {
    storageSetter({ type: "update" });
    const newShip = { ...ship };
    for (const resource of Object.keys(moveAmount)) {
      const resources = newShip.resources;
      resources[resource] -= moveAmount[resource];
      if (resources[resource] === 0) {
        delete resources[resource];
      }
    }
    setShip(newShip);
    setMoveMode(false);
  }

  return (
    <div className="row" style={{ alignItems: "unset" }}>
      {Object.entries(ship.resources).length > 0 ? (
        moveMode ? (
          <>
            <MoveResources ship={ship} onApplyMove={applyMove} />
            <button className="button red" onClick={() => setMoveMode(false)}>
              Cancel
            </button>
          </>
        ) : (
          <>
            <div>
              <ResourceList resources={ship.resources} />
            </div>
            <button className="button" onClick={() => setMoveMode(true)}>
              Move
            </button>
          </>
        )
      ) : (
        <div className="resource-row">Ship storage is empty</div>
      )}
    </div>
  );
}
