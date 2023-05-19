import "./ShipName.css";
import {useOutletContext} from "react-router-dom";
import {useHangarDispatchContext} from "../HangarContext";
import {useEffect, useState} from "react";

export function ShipName({ship}) {
    const context = useOutletContext();
    const jwt = context.jwt;
    const hangarSetter = useHangarDispatchContext();
    const [nameInputDisplay, setNameInputDisplay] = useState(false);
    const [shipName, setShipName] = useState(ship.name);

    async function renameShip(name) {
        await fetch(`/ship/${ship.id}`, {
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

    useEffect(()=>{
        setShipName(ship.name);
    }, [ship])


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
