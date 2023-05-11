import "./Messages.css";
import {useMessageContext} from "../MessageContext";

import {ResourceNeeded} from "./ResourceNeeded";
import {DisplayMinerShip} from "./DisplayMinerShip";

const Messages = () => {
    const message = useMessageContext();

    const checkStorage = (need, have) => {
        for (const resource of Object.keys(need)) {
            if (!(resource in have) || need[resource] > have[resource]) {
                return false;
            }
        }
        return true;
    }

    const setElement = () => {
        if (message.type) {
            switch (message.type) {
                case "miner cost":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage}/>);
                case "storage upgrade":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage}/>);
                case "hangar upgrade":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage}/>);
                case "display ship":
                    return (<DisplayMinerShip message={message} checkStorage={checkStorage}/>);
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