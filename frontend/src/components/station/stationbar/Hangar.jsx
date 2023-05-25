import "./Hangar.css";
import { useEffect, useState } from "react";
import { useMessageDispatchContext } from "../MessageContext";
import { useHangarContext } from "../HangarContext";
import { useNavigate, useOutletContext } from "react-router-dom";

function Hangar() {
  const { stationId } = useOutletContext();
  const navigate = useNavigate();
  const [minerCost, setMinerCost] = useState(null);
  const [upgradeCost, setUpgradeCost] = useState(null);
  const [storage, setStorage] = useState(null);
  const dispatch = useMessageDispatchContext();
  const update = useHangarContext();
  const [hangar, setHangar] = useState();

  useEffect(() => {
    fetch(`/api/v1/base/${stationId}/hangar`)
      .then((res) => res.json())
      .then((data) => {
        setHangar(data);

      })
      .catch((err) => console.error(err));
  }, [update, stationId]);

  function getShipCost() {
    fetch("/api/v1/ship/cost/miner")
      .then((res) => res.json())
      .then((data) => {
        setMinerCost(data);
      })
      .catch((err) => console.error(err));
  }

  function getHangarUpgradeCost() {
    fetch(`/api/v1/base/${stationId}/hangar/upgrade`)
      .then((res) => res.json())
      .then((data) => {
        setUpgradeCost(data);
      })
      .catch((err) => console.error(err));
  }

  function getShip(id) {
    fetch(`/api/v1/ship/miner/${id}`)
      .then((res) => res.json())
      .then((data) => {
        dispatch({
          type: "display ship",
          data: data,
          storage: storage,
        });
      })
      .catch((err) => console.error(err));
  }

  function getStorage() {
    fetch(`/api/v1/base/${stationId}/storage/resources`)
      .then((res) => res.json())
      .then((data) => {
        setStorage(data);
      })
      .catch((err) => console.error(err));
  }

  useEffect(() => {
    if (upgradeCost && storage) {
      dispatch({
        type: "base hangar upgrade",
        data: upgradeCost,
        storage: storage,
      });
      setUpgradeCost(null);
      setStorage(null);
    }
  }, [upgradeCost, storage]);

  useEffect(() => {
    if (minerCost && storage) {
      dispatch({
        type: "miner cost",
        data: minerCost,
        storage: storage,
      });
      setMinerCost(null);
      setStorage(null);
    }
  }, [minerCost, storage]);

  return (
    <div className="hangar">
      {!hangar ? (
        <div>Loading...</div>
      ) : (
        <>
          <div className="menu">
            <div>
              {"HANGAR | " +
                "lvl: " +
                hangar.level +
                " | " +
                (hangar.capacity - hangar.freeDocks) +
                " / " +
                hangar.capacity}
            </div>
            <div
              className="button"
              onClick={async () => {
                await getStorage();
                getHangarUpgradeCost();
              }}
            >
              Upgrade
            </div>
          </div>
          <div className="ship-list">
            {Object.keys(hangar.ships).length === 0 ? (
              <p>No ships yet</p>
            ) : (
              hangar.ships.map((ship) => {
                return (
                  <div className="shiplist"
                    onClick={() => {
                      navigate(`ship/${ship.id}`);
                    }}
                    key={ship.id}
                  >
                    {ship.name} - {ship.type}
                  </div>
                );
              })
            )}
          </div>
          <div className="add ship">
            <div
              className="button"
              onClick={() => {
                getStorage();
                getShipCost();
              }}
            >
              Add ship
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default Hangar;
