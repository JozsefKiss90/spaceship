import "./Storage.css";
import {useEffect, useState} from "react";
import {useStorageContext} from "../StorageContext";
import {useMessageDispatchContext} from "../MessageContext";
import {useOutletContext} from "react-router-dom";

const Storage = () => {
    const context = useOutletContext();
    const jwt = context.jwt;
    const stationId = context.stationId;
    const update = useStorageContext();
    const dispatch = useMessageDispatchContext();
    const [updateStorage, setUpdateStorage] = useState(null)
    const [upgradeCost, setUpgradeCost] = useState(null);
    const [storage, setStorage] = useState(null);

    useEffect(() => {
        fetch(`/base/${stationId}/storage`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwt}`
            }
        })
            .then(res => res.json())
            .then(data => setStorage(data))
            .catch(err => console.error(err));
    }, [update, updateStorage]);



    const getUpgrade = () => {
        fetch(`/base/${stationId}/storage/upgrade`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwt}`
            }
        })
            .then(res => res.json())
            .then(data => setUpgradeCost(data))
            .then(err => console.error(err));
    }

    useEffect(() => {
        if (upgradeCost) {
            dispatch({
                type: "base storage upgrade",
                data: upgradeCost,
                storage: storage.resources
            })
        }
    }, [upgradeCost])

    return (<>
        {!storage ? "Loading..." : (<div className="storage">
            <div className="menu">
                <div>{"STORAGE | " + "lvl: " + storage.level + " | " + (storage.capacity - storage.freeSpace) + " / " + storage.capacity}</div>
                <div className="button" onClick={() => {
                    setUpdateStorage(!updateStorage);
                    getUpgrade();
                }}>Upgrade
                </div>
            </div>
            {Object.keys(storage.resources).length === 0 ? (
                <div className="resource-row">No resources yet</div>) : (Object.keys(storage.resources).map(key => {
                return (<div className="resource-row" key={key}>
                    <img style={{width: "25px", height: "25px"}} src={'/' + key.toLowerCase() + '.png'}
                         alt={key}/>
                    <div style={{marginLeft: "5px"}} key={key}>{key}: {storage.resources[key]}</div>
                </div>);
            }))}
        </div>)}
    </>);
}

export default Storage;