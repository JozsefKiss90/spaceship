import "./Messages.css";
import {useMessageContext} from "../MessageContext";

import {ResourceNeeded} from "./ResourceNeeded";
import DisplayMinerShip from "./DisplayMinerShip";
import Mission from "./Mission";

const Messages = () => {
    const message = useMessageContext();



    const setElement = () => {
        if (message.type) {
            switch (message.type) {
                case "miner cost":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage}/>);
                case "base storage upgrade":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage}/>);
                case "base hangar upgrade":
                    return (<ResourceNeeded message={message} checkStorage={checkStorage}/>);
                case "display ship":
                    return (<DisplayMinerShip message={message} checkStorage={checkStorage}/>);
                case 'mission':
                    return (<Mission message={message}/>);
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
