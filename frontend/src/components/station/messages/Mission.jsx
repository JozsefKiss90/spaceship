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
      method: "PATCH",
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
      })
      .then((data) => setMission(data))
      .catch((err) => console.error(err));
  }

  function abortMission() {
    fetch(`/api/v1/mission/${id}/abort`, {
      method: "PATCH",
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
  const active = mission.status !== "OVER" && mission.status !== "ARCHIVED";
  const endTime = new Date(mission.approxEndTime + "Z");
  const nextReport = new Date(mission.currentObjectiveTime + "Z");
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
              date={nextReport}
              onComplete={onComplete}
              daysInHours={true} />
          </div>}
        <div className="mission-end">
          <div>{active ? "Expected mission completion" : "Mission ended"}</div>
          <div>{endTime.toLocaleString("hu-HU")}</div>
        </div>
        <div className="mission-reports">
          <div>Reports:</div>
          {mission.reports.map((report) => (
            <Report key={report.message} report={report} />
          ))}
        </div>
        <div className="mission-actions">
          {(active && mission.status !== "RETURNING") &&
            <button className="button red" onClick={abortMission}>
              Abort
            </button>}
          {mission.status === "OVER" && (
            <button className="button" onClick={archiveMission}>
              Archive
            </button>
          )}
        </div>
      </div>
    </>
  );

  function Report({ report }) {
    const formattedDate = `<${new Date(report.time + 'Z').toLocaleString("hu-HU")}>`;
    return <div className="mission-report">
      <div>{formattedDate}</div>
      <div>{report.message}</div>
    </div>
  }

}
