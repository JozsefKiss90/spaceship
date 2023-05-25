import {useMessageDispatchContext} from "../MessageContext";
import {useStorageDispatchContext} from "../StorageContext";
import {useHangarDispatchContext} from "../HangarContext";
import {useEffect, useState} from "react";
import {useOutletContext} from "react-router-dom";

export function ResourceNeeded({cost, item, onConfirm}) {
    const {stationId} = useOutletContext();
    // const messageSetter = useMessageDispatchContext();
    // const storageSetter = useStorageDispatchContext();
    // const hangarSetter = useHangarDispatchContext();
    const [storage, setStorage] = useState(null);
    //const [displayInput, setDisplayInput] = useState(false);
    // const [shipName, setShipName] = useState("");

    const checkStorage = () => {
        for (const resource of Object.keys(cost)) {
            if (!(resource in storage) || cost[resource] > storage[resource]) {
                return false;
            }
        }
        return true;
    }

    useEffect(() => {
        fetch(`/api/v1/base/${stationId}/storage/resources`)
            .then(res => res.json())
            .then(data => setStorage(data))
            .catch(err => console.error(err));
    }, [stationId])

    // const getShipName = () => {
    //     return (<>
    //         <div className="button" onClick={() => addShip(shipName)}>I want it!</div>
    //         <input type="text" value={shipName} onChange={(e) => setShipName(e.target.value)}
    //                placeholder="Enter ship name"></input>
    //     </>)
    // }

    // const addShip = async (name) => {
    //     await fetch(`/api/v1/base/${stationId}/add/ship`, {
    //         method: "POST", headers: {
    //             "Content-Type": "application/json"
    //         }, body: JSON.stringify({name: name, color: "EMERALD", type: "miner"})
    //     });
    //     setDisplayInput(false);
    //     messageSetter({type: null});
    //     storageSetter({type: "update"});
    //     hangarSetter({type: "update"});
    // }

    // const upgradeStorage = async () => {
    //     await fetch(`/api/v1/base/${stationId}/storage/upgrade`, {
    //         method: "POST", headers: {
    //             "Content-Type": "application/json"
    //         }, body: JSON.stringify({})
    //     });
    //     messageSetter({type: null});
    //     storageSetter({type: "update"});
    // }

    // const upgradeHangar = async () => {
    //     await fetch(`/api/v1/base/${stationId}/hangar/upgrade`, {
    //         method: "POST", headers: {
    //             "Content-Type": "application/json"
    //         }, body: JSON.stringify({})
    //     });
    //     messageSetter({type: null});
    //     storageSetter({type: "update"});
    //     hangarSetter({type: "update"});
    // }




    // function setFunction() {
    //     switch (item) {
    //         case 'miner cost':
    //             setDisplayInput(true);
    //             break;
    //         case 'base storage upgrade':
    //             return upgradeStorage();
    //         case 'base hangar upgrade':
    //             return upgradeHangar();
    //         case 'drill upgrade':
    //         case 'engine upgrade':
    //         case 'storage upgrade':
    //         case 'shield upgrade':
    //             console.log(message.type);
    //             return upgradePart(message.id, message.part);
    //     }
    // }

    if (storage===null) {
        return <div>Loading...</div>
    }

    return (<div className="cost">
        <div>Resources needed to upgrade {item}:</div>
        {Object.keys(cost).map(key => {
            return <div className="message-row" key={key}>
                <img style={{width: "25px", height: "25px"}} src={'/' + key.toLowerCase() + '.png'}
                     alt={key}/>
                <p style={{marginLeft: "5px"}}>{key}: {cost[key]}</p>
            </div>
        })}
        {checkStorage()
            ? <div className="button" onClick={onConfirm}>I want it!</div>
            : <div style={{color: "red"}}>Not enough resources</div>}
    </div>);
}
