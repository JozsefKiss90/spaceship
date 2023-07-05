import "./Storage.css";
import { useCallback, useEffect, useState } from "react";
import { useStorageContext } from "../StorageContext";
import { useNavigate, useOutletContext } from "react-router-dom";
import useHandleFetchError from "../../../hooks/useHandleFetchError";
import { useNotificationsDispatch } from "../../notifications/NotificationContext";

const Storage = () => {
    const { stationId } = useOutletContext();
    const navigate = useNavigate();
    const handleFetchError = useHandleFetchError();
    const notifDispatch = useNotificationsDispatch();
    const update = useStorageContext();
    const [storage, setStorage] = useState(null);

    const fetchStorage = useCallback(async () => {
        try {
            const res = await fetch(`/api/v1/base/${stationId}/storage`);
            if (res.ok) {
              const data = await res.json();
              setStorage(data);
            } else {
              handleFetchError(res);
            }
          } catch (err) {
            console.error(err);
            notifDispatch({
              type: "generic error",
            });
          }
    }, [stationId, handleFetchError, notifDispatch]);

    useEffect(() => {
        fetchStorage();
    }, [update, fetchStorage]);

    if (storage === null) {
        return (
            <div className="storage">
                <div className="menu">STORAGE loading...</div>
            </div>
        );
    }
    return (<>
        {!storage ? "Loading..." : (<div className="storage">
            <div className="menu">
                <div>{"STORAGE | lvl: " + storage.level + " | " + (storage.capacity - storage.freeSpace) + " / " + storage.capacity}</div>
                <div
                    className="button"
                    onClick={() => navigate("/station/upgrade/storage")}>Upgrade
                </div>
            </div>
            {Object.keys(storage.resources).length === 0 ? (
                <div className="resource-row">No resources yet</div>) : (Object.keys(storage.resources).map(key => {
                    return (<div className="resource-row" key={key}>
                        <img style={{ width: "25px", height: "25px" }} src={'/' + key.toLowerCase() + '.png'}
                            alt={key} />
                        <div style={{ marginLeft: "5px" }} key={key}>{key}: {storage.resources[key]}</div>
                    </div>);
                }))}
        </div>)}
    </>);
}

export default Storage;