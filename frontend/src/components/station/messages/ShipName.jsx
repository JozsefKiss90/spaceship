import "./ShipName.css";
import {useOutletContext} from "react-router-dom";
import {useHangarDispatchContext} from "../HangarContext";
import {useState} from "react";

export function ShipName({message}) {
    const context = useOutletContext();
    const jwt = context.jwt;
    const hangarSetter = useHangarDispatchContext();
    const [nameInputDisplay, setNameInputDisplay] = useState(false);
    const [shipName, setShipName] = useState(message.data.name);

    async function renameShip(name) {
        await fetch(`/ship/${message.data.id}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${jwt}`,
            },
            body: JSON.stringify({
                name: name,
            }),
        });
        setNameInputDisplay(false);
        hangarSetter({type: "update"});
    }


    return (<>
        <div className="ship-name row">
            {nameInputDisplay
                ? <div className="name-input-div">
                    <input className="name-input"
                        type="text"
                        value={shipName}
                        onChange={(e) => setShipName(e.target.value)}
                        placeholder="Enter ship name"
                        size="7"
                    ></input>
                    <div className="button" onClick={() => renameShip(shipName)}>
                        Change!
                    </div>
                </div>
                : <>
                    <div>{shipName}</div>
                    <div className="button" onClick={() => {
                        setNameInputDisplay(true);
                    }}>Rename
                    </div>
                </>
            }
        </div>
    </>)

}