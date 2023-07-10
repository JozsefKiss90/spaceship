import { useState } from "react";
import "./Location.css";
import { useNavigate } from "react-router-dom";
import useHandleFetchError from "../../../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

export default function Location({ location, availableShips }) {
  const navigate = useNavigate();
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();
  const [missionMenuToggle, setMissionMenuToggle] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  function onSubmitMission(e) {
    e.preventDefault();
    setSubmitting(true);
    const formData = new FormData(e.target);
    const entries = [...formData.entries()];
    const details = entries.reduce((acc, entry) => {
      const [k, v] = entry;
      acc[k] = v;
      return acc;
    }, {});
    details.locationId = location.id;
    startMission(details);
  }

  async function startMission(details) {
    try {
      const res = await fetch("/api/v1/mission/miner", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(details),
      });
      if (res.ok) {
        const data = await res.json();
        navigate(`/station/mission/${data.id}`);
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      notifDispatch({
        type: "generic error",
      });
    }
    setSubmitting(false);
  }

  return (
    <div className="location">
      <div className="loc-details">
        <img
          src="/planet.png"
          alt="planet"
          style={{ width: "100px", height: "100px" }}
        />
        <div className="loc-data">
          <div className="loc-name">Name: {location.name}</div>
          <div>Resource: {location.resourceType}</div>
          <div>Distance: {location.distanceFromStation} astronomical units</div>
          <div>Reserves: {location.resourceReserve}</div>
        </div>
        {!missionMenuToggle && (
          <div className="loc-actions">
            {location.missionId === 0 ? (
              <button
                className="button"
                onClick={() => setMissionMenuToggle(true)}
              >
                Start mission
              </button>
            ) : (
              <button
                className="button"
                onClick={() =>
                  navigate(`/station/mission/${location.missionId}`)
                }
              >
                Check mission
              </button>
            )}
          </div>
        )}
      </div>
      {missionMenuToggle && (
        <form className="loc-mission-menu" onSubmit={onSubmitMission}>
          <div>
            <label htmlFor="shipId">Ship: </label>
            <select name="shipId" required>
              {availableShips.length > 0 ? (
                availableShips.map((ship) => (
                  <option key={ship.id} value={ship.id}>
                    {ship.name}
                  </option>
                ))
              ) : (
                <option disabled>No available ships</option>
              )}
            </select>
          </div>
          <div>
            <label htmlFor="activityDuration">Mine for: </label>
            <select name="activityDuration" required>
              <option value={3600}>1 hour</option>
              <option value={7200}>2 hours</option>
              <option value={10800}>3 hours</option>
              <option value={14400}>4 hours</option>
              <option value={60}>DEMO</option>
            </select>
          </div>
          <div className="loc-mission-actions">
            <button className="button" type="submit" disabled={submitting}>
              Start
            </button>
            <button
              className="button red"
              onClick={() => setMissionMenuToggle(false)}
            >
              Cancel
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
