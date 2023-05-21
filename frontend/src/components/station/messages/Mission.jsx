import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export default function Mission() {
  const { id } = useParams();
  const [mission, setMission] = useState(null);

  useEffect(() => {
    fetch(`/api/mission/${id}`)
      .then((res) => res.json())
      .then((data) => setMission(data));
  }, [id]);

  if (mission === null) {
    return <div>Loading...</div>
  }

  return (
    <>
      <div>
        <div>Mission status: {mission.status}</div>
        <div>Mission type: {mission.missionType}</div>
        <div>Location: {mission.location}</div>
        <div>
          Events:{" "}
          {mission.eventLog.split("\n").map((log) => (
            <div>{log}</div>
          ))}
        </div>
        <div>
          Mission end: {new Date(mission.approxEndTime).toLocaleString('hu-HU')}
        </div>
      </div>
    </>
  );
}
