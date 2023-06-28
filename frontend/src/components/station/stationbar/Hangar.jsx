import "./Hangar.css";
import { useCallback, useEffect, useState } from "react";
import { useHangarContext } from "../HangarContext";
import { useNavigate, useOutletContext } from "react-router-dom";
import useHandleFetchError from "../../useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

function Hangar() {
  const { stationId } = useOutletContext();
  const navigate = useNavigate();
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();
  const update = useHangarContext();
  const [hangar, setHangar] = useState(null);

  const fetchHangar = useCallback(async () => {
    try {
      const res = await fetch(`/api/v1/base/${stationId}/hangar`);
      if (res.ok) {
        const data = await res.json();
        setHangar(data);
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
  }, [stationId, handleFetchError, notifDispatch]);

  useEffect(() => {
    fetchHangar();
  }, [update, fetchHangar]);

  if (hangar === null) {
    return (
      <div className="hangar">
        <div className="menu">HANGAR loading...</div>
      </div>
    );
  }
  return (
    <div className="hangar">
      <div className="menu">
        <div>
          {"HANGAR | lvl: " +
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
          hangar.ships
            .sort((a, b) => a.id - b.id)
            .map((ship) => {
              return (
                <div
                  className="shiplist"
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
    </div>
  );
}

export default Hangar;
