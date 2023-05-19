import { useState } from "react";
import "./Location.css";
import { useHangarContext } from "../HangarContext";
import { useNavigate, useOutletContext } from "react-router-dom";

export default function Location({ location }) {
  const [missionMenuToggle, setMissionMenuToggle] = useState(false);
  const { jwt } = useOutletContext();
  const hangar = useHangarContext();
  const navigate = useNavigate();

  console.log(hangar);

  function onSubmitMission(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const entries = [...formData.entries()];
    const details = entries.reduce((acc, entry) => {
      const [k, v] = entry;
      acc[k] = v;
      return acc;
    }, {});
    details.locationId = location.id;
    postMission(details);
  }

  function postMission(details) {
    fetch("/api/mission", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
      },
      body: JSON.stringify(details),
    })
      .then((res) => {
        if(res.ok) {
            return res.json();
        } else {
            throw new Error(res.status);
        }
      })
      .then((data) => {
        navigate(`/station/mission/${data.id}`);
      })
      .catch((e) => {
        console.error(e);});
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
        </div>
        {!missionMenuToggle && (
          <div className="loc-actions">
            {location.missionId === 0 ? (
              <button onClick={() => setMissionMenuToggle(true)}>
                Send mission
              </button>
            ) : (
              <button>Check Mission</button>
            )}
          </div>
        )}
      </div>
      {missionMenuToggle && (
        <form className="loc-mission-menu" onSubmit={onSubmitMission}>
          <div>
            <label htmlFor="shipId">Ship: </label>
            <select name="shipId" required></select>
          </div>
          <div>
            <label htmlFor="activityDuration">Mine for: </label>
            <select name="activityDuration" required>
              <option value={3600}>1 hour</option>
              <option value={7200}>2 hours</option>
              <option value={10800}>3 hours</option>
              <option value={14400}>4 hours</option>
            </select>
          </div>
          <div className="loc-mission-actions">
            <button type="submit">Send</button>
            <button type="button" onClick={() => setMissionMenuToggle(false)}>
              Cancel
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
