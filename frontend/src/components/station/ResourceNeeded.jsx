import {useReducer} from "react";
import {useMessageDispatchContext} from "../MessageContext";


export function ResourceNeeded({message, checkStorage, func}) {
    const messageSetter = useMessageDispatchContext();
    function setText()  {
        switch (message.type) {
            case 'miner cost':
                return "to add ship";
            case 'storage upgrade':
                return "to upgrade storage";
            case 'hangar upgrade':
                return "to upgrade hangar";
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
        {checkStorage(message.data, message.storage) ?
            <div className="button" onClick={func}>Confirm</div> :
            <div style={{color: "red"}}>Not enough resource</div>}
    </div>);
}