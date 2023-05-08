import "./Storage.css";
import {useEffect, useState} from "react";
import {useStorageContext} from "../StorageContext";
import {useMessageDispatchContext} from "../MessageContext";

const Storage = () => {
    const update = useStorageContext();
    const dispatch = useMessageDispatchContext();
    const [updateStorage, setUpdateStorage] = useState(null)
    const [upgradeCost, setUpgradeCost] = useState(null);
    const [storage, setStorage] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/base/storage')
            .then(res => res.json())
            .then(data => setStorage(data))
            .catch(err => console.error(err));
    }, [update, updateStorage]);



    const getUpgrade = () => {
        fetch("http://localhost:8080/base/storage/upgrade")
            .then(res => res.json())
            .then(data => setUpgradeCost(data))
            .then(err => console.log(err));
    }

    useEffect(() => {
        if (upgradeCost) {
            dispatch({
                type: "storage upgrade",
                data: upgradeCost,
                storage: storage.resources
            })
        }
    }, [upgradeCost])

    return (<>
        {!storage ? "Loading..." : (<div className="storage">
            <div className="menu">
                <div>{"STORAGE | " + storage.level + " | " + (storage.capacity - storage.freeSpace) + " / " + storage.capacity}</div>
                <div className="button" onClick={() => {
                    setUpdateStorage(!updateStorage);
                    getUpgrade();
                }}>Upgrade
                </div>
            </div>
            {Object.keys(storage.resources).length === 0 ? (
                <div>No resources yet</div>) : (Object.keys(storage.resources).map(key => {
                return (<div className="resource-row" key={key}>
                    <img style={{width: "25px", height: "25px"}} src={key.toLowerCase() + '.png'}
                         alt={key}/>
                    <p style={{marginLeft: "5px"}} key={key}>{key}: {storage.resources[key]}</p>
                </div>);
            }))}
        </div>)}
    </>);
}

export default Storage;