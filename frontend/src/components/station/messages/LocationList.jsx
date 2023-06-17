import { useEffect, useState } from "react";
import { useOutletContext } from "react-router-dom";
import "./LocationList.css";
import Location from "./Location";

export default function LocationList() {
  const { user, stationId } = useOutletContext();
  const [locations, setLocations] = useState(null);
  const [availableShips, setAvailableShips] = useState(null);

  useEffect(() => {
    Promise.all([
      fetch(`/api/v1/location?user=${user.userId}`).then((res) => res.json()),
      fetch(`/api/v1/base/${stationId}/hangar`).then((res) => res.json()),
    ]).then(([locations, hangar]) => {
      setLocations(locations.sort((a, b) => a.id - b.id));
      setAvailableShips(hangar.ships.filter(ship => ship.missionId === 0));
    });
  }, [user, stationId]);

  if (locations === null || availableShips === null) {
    return <div>Loading...</div>;
  }

  return (
    <div className="location-list">
      <div>Available locations:</div>
      {locations.map((location) => (
        <Location key={location.id} location={location} availableShips={availableShips} />
      ))}
    </div>
  );
}
