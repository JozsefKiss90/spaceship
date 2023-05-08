import "./Messages.css";
import {useMessageContext, useMessageDispatchContext} from "../MessageContext";
import {useStorageDispatchContext} from "../StorageContext";
import {useHangarDispatchContext} from "../HangarContext";
import {ResourceNeeded} from "./ResourceNeeded";

const Messages = () => {
    const message = useMessageContext();
    const messageSetter = useMessageDispatchContext();
    const storageSetter = useStorageDispatchContext();
    const hangarSetter = useHangarDispatchContext();

    const checkStorage = (need, have) => {
        for (const resource of Object.keys(need)) {
            if (!(resource in have) || need[resource] > have[resource]) {
                return false;
            }
        }
        return true;
    }

    const addShip = async () => {
        await fetch("http://localhost:8080/base/1/add/ship", {
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({name: "pju-pju", color: "EMERALD", type: "miner"})
        });
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

    const setElement = () => {
        if (message.type) {
            switch (message.type) {
                case "miner cost":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage} func={addShip}/>);
                case "storage upgrade":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage} func={upgradeStorage}/>);
                case "hangar upgrade":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage} func={upgradeHangar}/>);
                default:
                    return "Howdy, Commander! Command something...";
            }
        } else return "Howdy, Commander! Command something...";
    }


    return (<div className="message-log">
        <div className="messages">{setElement()}</div>
    </div>);
}

export default Messages;