import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./Mission.css";

export default function Mission() {
  const { id } = useParams();
  const [mission, setMission] = useState(null);

  useEffect(() => {
    fetch(`/api/v1/mission/${id}`)
      .then((res) => res.json())
      .then((data) => setMission(data));
  }, [id]);

  function archiveMission() {
    fetch(`/api/v1/mission/${id}/archive`, {
      method: "PUT",
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
      })
      .then((data) => setMission(data))
      .catch((err) => console.error(err));
  }

  if (mission === null) {
    return <div>Loading...</div>;
  }

  const formattedStatus = mission.status.replaceAll("_", " ");
  return (
    <>
      <div className="mission-container">
        <div className="mission-title">{`${mission.missionType} mission on ${mission.location}`}</div>
        <div className="mission-data">
          <div>
            <div>Location</div>
            <div>{mission.location}</div>
          </div>
          <div className="mission-status">{formattedStatus}</div>
          <div>
            <div>Ship</div>
            <div>{mission.shipName}</div>
          </div>
        </div>
        {mission.status !== "OVER" && mission.status !== "ARCHIVED" ? (
          <div className="mission-times">
            <div className="mission-timer">
              <div>Expected mission completion</div>
              <div>
                {new Date(mission.approxEndTime).toLocaleString("hu-HU")}
              </div>
            </div>
            <div className="mission-timer">
              <div>Next expected report</div>
              <div>
                {new Date(mission.currentObjectiveTime).toLocaleString("hu-HU")}
              </div>
            </div>
          </div>
        ) : (
          <div className="mission-end">
            <div>Mission ended</div>
            <div>{new Date(mission.approxEndTime).toLocaleString("hu-HU")}</div>
          </div>
        )}
        <div className="mission-reports">
          <div>Reports:</div>
          {mission.eventLog.map((log) => (
            <div key={log}>{log}</div>
          ))}
        </div>
        <div className="mission-actions">
          {mission.status === "OVER" && (
            <button className="button" onClick={archiveMission}>
              Archive
            </button>
          )}
        </div>
      </div>
    </>
  );
}
