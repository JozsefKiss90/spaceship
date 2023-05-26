import { useCallback, useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import Countdown from "react-countdown";
import "./Mission.css";

export default function Mission() {
  const { id } = useParams();
  const [mission, setMission] = useState(null);
  const timer = useRef();

  const fetchMission = useCallback(() => {
    return fetch(`/api/v1/mission/${id}`)
      .then((res) => res.json())
      .then((data) => setMission(data));
  }, [id]);

  useEffect(() => {
    fetchMission();
  }, [fetchMission]);

  async function onComplete() {
    await fetchMission();
    timer.current.getApi().start();
  }

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
  const active = mission.status !== "OVER" && mission.status !== "ARCHIVED"
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
        {active &&
          <div className="mission-timer">
            <div>Next expected report</div>
            <Countdown
              ref={timer}
              className="mission-countdown"
              date={mission.currentObjectiveTime}
              onComplete={onComplete} />
          </div>}
        <div className="mission-end">
          <div>{active ? "Expected mission completion" : "Mission ended"}</div>
          <div>{new Date(mission.approxEndTime).toLocaleString("hu-HU")}</div>
        </div>
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
