import { useCallback, useEffect, useState } from "react";
import { useNavigate, useOutletContext } from "react-router-dom";
import { useStorageDispatchContext } from "../StorageContext";
import { useHangarDispatchContext } from "../HangarContext";
import "./AddShip.css";
import ResourceList from "./ResourceList";

export default function AddShip() {
  const { stationId } = useOutletContext();
  const navigate = useNavigate();
  const storageSetter = useStorageDispatchContext();
  const hangarSetter = useHangarDispatchContext();
  const [shipType, setShipType] = useState("miner");
  const [cost, setCost] = useState(null);
  const [colors, setColors] = useState(null);
  const [resources, setResources] = useState(null);
  const [hasAvailableDock, setHasAvailableDock] = useState(false);
  const [loading, setLoading] = useState(true);

  const getShipCost = useCallback(() => {
    setLoading(true);
    fetch(`/api/v1/ship/cost/${shipType}`)
      .then((res) => res.json())
      .then((data) => {
        setCost(data);
        setLoading(false);
      })
      .catch((err) => console.error(err));
  }, [shipType]);

  useEffect(() => {
    Promise.all([
      fetch(`/api/v1/ship/color`).then((res) => res.json()),
      fetch(`/api/v1/base/${stationId}/storage/resources`).then((res) =>
        res.json()
      ),
      fetch(`/api/v1/base/${stationId}/hangar`).then((res) => res.json()),
    ]).then(([colors, resources, hangar]) => {
      setColors(colors);
      setResources(resources);
      setHasAvailableDock(hangar.freeDocks > 0);
    });
  }, [stationId]);

  useEffect(() => {
    getShipCost();
  }, [getShipCost]);

  function checkStorage() {
    for (const resource of Object.keys(cost)) {
      if (!(resource in resources) || cost[resource] > resources[resource]) {
        return false;
      }
    }
    return true;
  }

  function onSubmit(e) {
    e.preventDefault();
    setLoading(true);
    const formData = new FormData(e.target);
    const entries = [...formData.entries()];

    const shipData = entries.reduce((acc, entry) => {
      const [k, v] = entry;
      acc[k] = v;
      return acc;
    }, {});
    addShip(shipData);
  }

  function addShip(shipData) {
    fetch(`/api/v1/base/${stationId}/add/ship`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(shipData),
    })
      .then((res) => res.json())
      .then((data) => {
        storageSetter({ type: "update" });
        hangarSetter({ type: "update" });
        navigate(`/station/ship/${data}`);
      })
      .catch((err) => console.error(err));
  }

  function AddButton() {
    if (!hasAvailableDock) {
      return (
        <div style={{ color: "red", textShadow: "1px 1px black" }}>
          No available dock in hangar
        </div>
      );
    } else if (!checkStorage()) {
      return (
        <div style={{ color: "red", textShadow: "1px 1px black" }}>
          Not enough resources
        </div>
      );
    } else {
      return (
        <div>
          <button className="button">Buy ship</button>
        </div>
      );
    }
  }

  if (colors === null || resources === null || hasAvailableDock === null) {
    return <div>Loading...</div>;
  }

  return (
    <div className="add-ship">
      <h2>Buy new ship:</h2>
      <form onSubmit={onSubmit}>
        <div>
          <label htmlFor="name">Name: </label>
          <input
            name="name"
            id="name"
            type="text"
            placeholder="Enter ship name"
            size="15"
            minLength="3"
            maxLength="15"
            required
          ></input>
        </div>
        <div>
          <label htmlFor="type">Type: </label>
          <select
            name="type"
            id="type"
            onChange={(e) => setShipType(e.target.value)}
          >
            <option value="miner">Miner</option>
          </select>
        </div>
        <div>
          <label htmlFor="color">Color: </label>
          <select name="color" id="color">
            {colors.map((color) => (
              <option key={color}>{color}</option>
            ))}
          </select>
        </div>
        {!loading ? (
          <>
            <ResourceList message={"Resources needed:"} cost={cost} />
            <AddButton />
          </>
        ) : (
          <div>Loading...</div>
        )}
      </form>
    </div>
  );
}
