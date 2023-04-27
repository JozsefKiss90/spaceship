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
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({name: "pju-pju", color: "EMERALD"})
        });
        console.log(await response.json());
        storageSetter({type: "update"});
        hangarSetter({type: "update"});
        messageSetter({type: null});
    }
    const upgradeStorage = async () => {
        const response = await fetch("http://localhost:8080/base/upgrade/storage", {
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({})
        });
        console.log(await response.json());
        storageSetter({type: "update"});
        hangarSetter({type: "update"});
        messageSetter({type: null});
    }

    const upgradeHangar = async () => {
        const response = await fetch("http://localhost:8080/base/upgrade/hangar", {
            method: "POST", headers: {
                "Content-Type": "application/json",
            }, body: JSON.stringify({})
        });
        console.log(await response.json());
        storageSetter({type: "update"});
        hangarSetter({type: "update"});
        messageSetter({type: null});
    }

    const setElement = () => {
        if (message.type) {
            switch (message.type) {
                case "miner cost":
                    return (<div className="cost">
                        <div>Resources needed to add miner ship:</div>
                        {Object.keys(message.data).map(key => {

                            return <div className="message-row" key={key}>
                                <img style={{width: "25px", height: "25px"}} src={key.toLowerCase() + '.png'}
                                     alt={key}/>
                                <p style={{marginLeft: "5px"}}>{key}: {message.data[key]}</p>
                            </div>
                        })}
                        {message.type?checkStorage(message.data, message.storage) ?
                            <div className="button" onClick={addShip}>Confirm</div> :
                            <div style={{color: "red"}}>Not enough resource</div>:<div></div>}
                    </div>);
                case "storage upgrade":
                     return (<div className="cost">
                        <div>Resources needed to upgrade storage:</div>
                        {Object.keys(message.data).map(key => {
                            return <div className="message-row" key={key}>
                                <img style={{width: "25px", height: "25px"}} src={key.toLowerCase() + '.png'}
                                     alt={key}/>
                                <p style={{marginLeft: "5px"}}>{key}: {message.data[key]}</p>
                            </div>
                        })}
                        {message.type ? checkStorage(message.data, message.storage) ?
                            <div className="button" onClick={upgradeStorage}>Confirm</div> :
                            <div style={{color: "red"}}>Not enough resource</div>:<div></div>}
                    </div>);
                case "hangar upgrade":
                    return (<div className="cost">
                        <div>Resources needed to upgrade hangar:</div>
                        {Object.keys(message.data).map(key => {
                            return <div className="message-row" key={key}>
                                <img style={{width: "25px", height: "25px"}} src={key.toLowerCase() + '.png'}
                                     alt={key}/>
                                <p style={{marginLeft: "5px"}}>{key}: {message.data[key]}</p>
                            </div>
                        })}
                        {message.type ? checkStorage(message.data, message.storage) ?
                            <div className="button" onClick={upgradeHangar} >Confirm</div> :
                            <div style={{color: "red"}}>Not enough resource</div>:<div></div>}
                    </div>);
                default:
                    return "Howdy, Commander! Command something...";
            }
        } else return "Howdy, Commander! Command something...";
    }


    return <div className="message-log">
        <div className="messages">{setElement()}</div>
    </div>;
}


export default Messages;