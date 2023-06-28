import { useCallback, useEffect, useState } from "react";
import { useOutletContext } from "react-router-dom";
import "./LocationList.css";
import Location from "./Location";
import useHandleFetchError from "../../useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

export default function LocationList() {
  const { user, stationId } = useOutletContext();
  const handleFetchError = useHandleFetchError();
  const notifDispatch = useNotificationsDispatch();
  const [locations, setLocations] = useState(null);
  const [availableShips, setAvailableShips] = useState(null);

  const fetchLocationsAndShips = useCallback(async () => {
    try {
      const urls = [
        `/api/v1/location?user=${user.userId}`,
        `/api/v1/base/${stationId}/hangar`,
      ];
      const [locations, hangar] = await Promise.all(
        urls.map(async (url) => {
          const res = await fetch(url);
          if (res.ok) {
            return res.json();
          } else {
            handleFetchError(res);
          }
        })
      );
      setLocations(locations.sort((a, b) => a.id - b.id));
      setAvailableShips(
        hangar.ships
          .filter((ship) => ship.missionId === 0)
          .sort((a, b) => a.id - b.id)
      );
    } catch (err) {
      console.error(err);
      notifDispatch({
        type: "generic error",
      });
    }
  }, [user, stationId, handleFetchError, notifDispatch]);

  useEffect(() => {
    fetchLocationsAndShips();
  }, [fetchLocationsAndShips]);

  if (locations === null || availableShips === null) {
    return <div>Loading...</div>;
  }

  return (
    <div className="location-list">
      <div>Available locations:</div>
      {locations.map((location) => (
        <Location
          key={location.id}
          location={location}
          availableShips={availableShips}
        />
      ))}
    </div>
  );
}
