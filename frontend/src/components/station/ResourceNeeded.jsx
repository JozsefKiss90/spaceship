import {useMessageDispatchContext} from "../MessageContext";
import {useStorageDispatchContext} from "../StorageContext";
import {useHangarDispatchContext} from "../HangarContext";
import {useState} from "react";

export function ResourceNeeded({message, checkStorage}) {
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
        await fetch("http://localhost:8080/base/1/add/ship", {
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({name: name, color: "EMERALD", type: "miner"})
        });
        setDisplayInput(false);
        messageSetter({type: null});
        storageSetter({type: "update"});
        hangarSetter({type: "update"});

    }

    const upgradeStorage = async () => {
        await fetch("http://localhost:8080/base/1/storage/upgrade", {
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({})
        });
        messageSetter({type: null});
        storageSetter({type: "update"});

    }

    const upgradeHangar = async () => {
        await fetch("http://localhost:8080/base/1/hangar/upgrade", {
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({})
        });
        messageSetter({type: null});
        storageSetter({type: "update"});
        hangarSetter({type: "update"});

    }

    function setText() {
        switch (message.type) {
            case 'miner cost':
                return "to add ship";
            case 'storage upgrade':
                return "to upgrade storage";
            case 'hangar upgrade':
                return "to upgrade hangar";
        }
    }

    function setFunction() {
        switch (message.type) {
            case 'miner cost':
                setDisplayInput(true);
                break;
            case 'storage upgrade':
                return upgradeStorage();
            case 'hangar upgrade':
                return upgradeHangar();
        }
    }

    return (<div className="cost">
        <div>Resources needed {setText()}:</div>
        {Object.keys(message.data).map(key => {
            return <div className="message-row" key={key}>
                <img style={{width: "25px", height: "25px"}} src={key.toLowerCase() + '.png'}
                     alt={key}/>
                <p style={{marginLeft: "5px"}}>{key}: {message.data[key]}</p>
            </div>
        })}
        {displayInput ? <div>{getShipName()}</div> : checkStorage(message.data, message.storage) ?
            <div className="button" onClick={setFunction}>I want it!</div> :
            <div style={{color: "red"}}>Not enough resource</div>}
    </div>);
}