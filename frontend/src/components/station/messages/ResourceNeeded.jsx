import {useMessageDispatchContext} from "../MessageContext";
import {useStorageDispatchContext} from "../StorageContext";
import {useHangarDispatchContext} from "../HangarContext";
import {useState} from "react";
import {useOutletContext} from "react-router-dom";

export function ResourceNeeded({message, checkStorage}) {
    const { stationId } = useOutletContext();
    const messageSetter = useMessageDispatchContext();
    const storageSetter = useStorageDispatchContext();
    const hangarSetter = useHangarDispatchContext();
    const [displayInput, setDisplayInput] = useState(false);
    const [shipName, setShipName] = useState("");

    const getShipName = () => {
        return (<>
            <div className="button" onClick={() => addShip(shipName)}>I want it!</div>
            <input type="text" value={shipName} onChange={(e) => setShipName(e.target.value)}
                   placeholder="Enter ship name"></input>
        </>)
    }

    const addShip = async (name) => {
        await fetch(`/base/${stationId}/add/ship`, {
            method: "POST", 
            body: JSON.stringify({name: name, color: "EMERALD", type: "miner"})
        });
        setDisplayInput(false);
        messageSetter({type: null});
        storageSetter({type: "update"});
        hangarSetter({type: "update"});
    }

    const upgradeStorage = async () => {
        await fetch(`/base/${stationId}/storage/upgrade`, {
            method: "POST",
            body: JSON.stringify({})
        });
        messageSetter({type: null});
        storageSetter({type: "update"});
    }

    const upgradeHangar = async () => {
        await fetch(`/base/${stationId}/hangar/upgrade`, {
            method: "POST",
            body: JSON.stringify({})
        });
        messageSetter({type: null});
        storageSetter({type: "update"});
        hangarSetter({type: "update"});
    }

    const upgradePart = async (id, part) => {
        await fetch(`/ship/miner/${id}/upgrade?part=${part}`, {
            method: "PATCH",
            body: JSON.stringify({})
        });
        storageSetter({type: "update"});
    }

    function setText() {
        switch (message.type) {
            case 'miner cost':
                return "to add ship";
            case 'base storage upgrade':
                return "to upgrade storage";
            case 'base hangar upgrade':
                return "to upgrade hangar";
            case 'drill upgrade':
                return "to upgrade ship drill";
            case 'engine upgrade':
                return "to upgrade ship engine";
            case 'storage upgrade':
                return "to upgrade ship storage";
            case 'shield upgrade':
                return "to upgrade ship shield";
        }
    }

    function setFunction() {
        switch (message.type) {
            case 'miner cost':
                setDisplayInput(true);
                break;
            case 'base storage upgrade':
                return upgradeStorage();
            case 'base hangar upgrade':
                return upgradeHangar();
            case 'drill upgrade':
            case 'engine upgrade':
            case 'storage upgrade':
            case 'shield upgrade':
                console.log(message.type);
                return upgradePart(message.id, message.part);
        }
    }

    return (<div className="cost">
        <div>Resources needed {setText()}:</div>
        {Object.keys(message.data).map(key => {
            return <div className="message-row" key={key}>
                <img style={{width: "25px", height: "25px"}} src={'/'+ key.toLowerCase() + '.png'}
                     alt={key}/>
                <p style={{marginLeft: "5px"}}>{key}: {message.data[key]}</p>
            </div>
        })}
        {displayInput ? <div>{getShipName()}</div> : checkStorage(message.data, message.storage) ?
            <div className="button" onClick={setFunction}>I want it!</div> :
            <div style={{color: "red"}}>Not enough resource</div>}
    </div>);
}