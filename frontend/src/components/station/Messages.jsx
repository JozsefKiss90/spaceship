import "./Messages.css";
import {useMessageContext, useMessageDispatchContext} from "../MessageContext";
import {useStorageDispatchContext} from "../StorageContext";
import {useHangarDispatchContext} from "../HangarContext";

const Messages = () => {
    const message = useMessageContext();
    const messageSetter = useMessageDispatchContext();
    const storageSetter = useStorageDispatchContext();
    const hangarSetter = useHangarDispatchContext();

    const checkStorage = (need, have) => {
        for (const resource of Object.keys(need)) {
            console.log(resource in have);
            if (!(resource in have) || need[resource] > have[resource]) {
                return false;
            }
        }
        return true;
    }

    const addShip = async () => {
        const response = await fetch("http://localhost:8080/base/hangar/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: "pju-pju",color: "EMERALD"})
        });
        const ship = await response.json();
        console.log(ship);
        storageSetter({type: "update"});
        hangarSetter({type: "update"});
        messageSetter({});

    }
console.log(message);

    const element = message.data ? (<div className="cost">
        <div>Resources needed to add miner ship:</div>
        {Object.keys(message.data).map(key => {
            console.log(key);
            console.log(message.data[key]);
            return <p key={key}>{key}: {message.data[key]}</p>;
        })}
        {checkStorage(message.data, message.storage) ? <div className="button" onClick={addShip}>Confirm</div> :
            <div style={{color: "red"}}>Not enough resource</div>}
    </div>) : "Howdy, Commander! Do something...";


    return <div className="message-log">
        <div className="messages">{element}</div>
    </div>;
}


export default Messages;