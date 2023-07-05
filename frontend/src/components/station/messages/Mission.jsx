import { useCallback, useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import Countdown from "react-countdown";
import "./Mission.css";
import useHandleFetchError from "../../../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

export default function Mission() {
  const { id } = useParams();
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();
  const [mission, setMission] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const timer = useRef();

  const fetchMission = useCallback(async () => {
    try {
      const res = await fetch(`/api/v1/mission/${id}`);
      if (res.ok) {
        const data = await res.json();
        setMission(data);
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
  }, [id, handleFetchError, notifDispatch]);

  useEffect(() => {
    fetchMission();
  }, [fetchMission]);

  async function onComplete() {
    await fetchMission();
    timer.current.getApi().start();
  }

  async function archiveMission() {
    setSubmitting(true);
    try {
      const res = await fetch(`/api/v1/mission/${id}/archive`, {
        method: "PATCH",
      });
      if (res.ok) {
        const data = await res.json();
        setMission(data);
        notifDispatch({
          type: "add",
          message: "Mission archived.",
          timer: 5,
        });
      } else {
        handleFetchError(res);
      }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
    setSubmitting(false);
  }

  async function abortMission() {
    setSubmitting(true);
    try {
    const res = await fetch(`/api/v1/mission/${id}/abort`, {
      method: "PATCH",
    });
    if (res.ok) {
      const data = await res.json();
      setMission(data);
      notifDispatch({
        type: "add",
        message: "Mission aborted.",
        timer: 5,
      });
    } else {
      handleFetchError(res);
    }
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
    setSubmitting(false);
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
        {active && (
          <div className="mission-timer">
            <div>Next expected report</div>
            <Countdown
              ref={timer}
              className="mission-countdown"
              date={nextReport}
              onComplete={onComplete}
              daysInHours={true}
            />
          </div>
        )}
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
          {active && mission.status !== "RETURNING" && (
            <button className="button red" onClick={abortMission} disabled={submitting}>
              Abort
            </button>
          )}
          {mission.status === "OVER" && (
            <button className="button" onClick={archiveMission} disabled={submitting}>
              Archive
            </button>
          )}
        </div>
      </div>
    </>
  );

  function Report({ report }) {
    const formattedDate = `<${new Date(report.time + "Z").toLocaleString(
      "hu-HU"
    )}>`;
    return (
      <div className="mission-report">
        <div>{formattedDate}</div>
        <div>{report.message}</div>
      </div>
    );
  }
}
