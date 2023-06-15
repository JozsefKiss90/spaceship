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
            <button
              className="button"
              onClick={() => {
                navigate("/station/upgrade/hangar");
              }}
            >
              Upgrade
            </button>
          </div>
          <div className="ship-list">
            {Object.keys(hangar.ships).length === 0 ? (
              <p>No ships yet</p>
            ) : (
              hangar.ships.sort((a,b) => a.id - b.id).map((ship) => {
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
          <div className="add-ship-btn">
            <button
              className="button"
              onClick={() => {
                navigate("ship/add");
              }}
            >
              Add ship
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default Hangar;
