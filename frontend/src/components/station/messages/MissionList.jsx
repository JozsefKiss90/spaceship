import { useEffect, useState } from "react";
import "./MissionList.css";
import { useNavigate } from "react-router-dom";

export default function MissionList() {
  const [missions, setMissions] = useState(null);
  const [listToShow, setListToShow] = useState("active");
  const navigate = useNavigate();

  useEffect(() => {
    fetch(`/api/v1/mission/${listToShow}`)
      .then((res) => res.json())
      .then((data) => setMissions(data));
  }, [listToShow]);

  if (missions === null) {
    return <div>Loading</div>;
  }

  return (
    <div className="mission-list">
      <div className="ml-header">
        <div>Missions:</div>
        <div>
          <button
            className="ml-list-toggle"
            onClick={() => {
              setMissions(null);
              setListToShow("active");
            }}
            disabled={listToShow === "active"}
          >
            Active
          </button>
          <span> | </span>
          <button
            className="ml-list-toggle"
            onClick={() => {
              setMissions(null);
              setListToShow("archived");
            }}
            disabled={listToShow === "archived"}
          >
            Archived
          </button>
        </div>
      </div>
      {missions.map((mission) => {
        return mission.status === "ARCHIVED" ? (
          <ArchivedMissionListItem key={mission.id} mission={mission} />
        ) : (
          <ActiveMissionListItem key={mission.id} mission={mission} />
        );
      })}
    </div>
  );

  function ActiveMissionListItem({ mission }) {
    const formattedStatus = mission.status.replaceAll('_', ' ');
    const nextReport = new Date(mission.currentObjectiveTime+"Z");
    return (
      <div
        className="ml-li ml-li-active"
        onClick={() => navigate(`/station/mission/${mission.id}`)}
      >
        <div>
          <div>{`${mission.missionType} mission on ${mission.location}`}</div>
          {mission.status !== "OVER" && (
            <div>Next report : {nextReport.toLocaleString('hu-HU')}</div>
          )}
        </div>
        <div>{formattedStatus}</div>
      </div>
    );
  }

  function ArchivedMissionListItem({ mission }) {
    const endTime = new Date(mission.approxEndTime+"Z");
    return (
      <div
        className="ml-li"
        onClick={() => navigate(`/station/mission/${mission.id}`)}
      >
        <div>{`${mission.missionType} mission on ${mission.location}`}</div>
        <div>Ended: {endTime.toLocaleString('hu-HU')}</div>
      </div>
    );
  }
}
