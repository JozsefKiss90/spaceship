import "./DisplayMinerShip.css";
import {ResourceNeeded} from "./ResourceNeeded";
import {useEffect, useState} from "react";
import {useHangarDispatchContext} from "../HangarContext";
import {useOutletContext} from "react-router-dom";
import station from "../Station";
import {useMessageDispatchContext} from "../MessageContext";

export function DisplayMinerShip({message, checkStorage}) {
    const dispatch = useMessageDispatchContext();
    const [jwt, , user, , stationId] = useOutletContext();
    const ship = message.data;
    const [resource, setResource] = useState(null);
    const [storage, setStorage] = useState(null);
    const [displayResource, setDisplayResource] = useState(false);
    const [resourceMessage, setResourceMessage] = useState(null);
    const [show, setShow] = useState(false);
    const [part, setPart] = useState(null);
    const [nameInputDisplay, setNameInputDisplay] = useState(false);
    const [shipName, setShipName] = useState(ship.name);
    const [newShipName, setNewShipName] = useState("");
    const hangarSetter = useHangarDispatchContext();
    const [shipColors, setShipColors] = useState(null);
    const [color, setColor] = useState(ship.color);
    const [colorListDisplay, setColorListDisplay] = useState(false);
    const [listTodDisplay, setListToDisplay] = useState(<div>nothingToDisplayYet</div>);
    const [triggerColorChange, setTriggerColorChange] = useState(false);


    async function getShipPartUpgradeCost(part) {
        await fetch(`/ship/miner/${ship.id}/upgrade?part=${part}`, {
            method: "GET", headers: {
                "Authorization": `Bearer ${jwt}`
            }
        })
            .then(res => res.json())
            .then(data => setResource(data))
            .catch(err => console.error(err));
    }

    function getStorage() {
        fetch(`/base/${stationId}/storage/resources`, {
            method: "GET", headers: {
                "Authorization": `Bearer ${jwt}`
            }
        })
            .then(res => res.json())
            .then(data => setStorage(data))
            .catch(err => console.error(err));
    }

    async function onClick(part) {
        await getStorage();
        await getShipPartUpgradeCost(part);
        setPart(part);
        setDisplayResource(!displayResource);
    }

    function getShipName() {
        return (<div style={{display: "flex", width: "100%", flexFlow: "row", justifyContent: "space-between"}}>
            <input type="text" value={newShipName} onChange={(e) => setNewShipName(e.target.value)}
                   placeholder="Enter ship name"></input>
            <div className="button" onClick={() => renameShip(newShipName)}>Change!</div>
        </div>)
    }

    function changeColor() {
        setTriggerColorChange(true);
        setColorListDisplay(false);
    }


    async function getAvailableColors() {
        if (!shipColors) await getShipColors();
    }

    async function getShipColors() {
        await fetch(`/ship/color`, {
            method: "GET", headers: {
                "Authorization": `Bearer ${jwt}`
            }
        })
            .then(res => res.json())
            .then(data => setShipColors(data))
            .catch(err => console.error(err))
    }

    async function renameShip(name) {
        await fetch(`/ship/${ship.id}`, {
            method: "PATCH", headers: {
                "Content-Type": "application/json", "Authorization": `Bearer ${jwt}`
            }, body: JSON.stringify({
                name: name
            })
        });
        setShipName(newShipName);
        setNewShipName("");
        setNameInputDisplay(false);
        hangarSetter({type: "update"});
    }

    function sendShipToMission() {
        console.log("send ship to mission");
        // dispatch({
        //     type: "mission",
        //     ship: ship
        // })
        fetch(`/mission/`, {
            method: "POST", headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${jwt}`
            }, body: JSON.stringify({
                shipId: ship.id,
                locationId: 1,
                activityDuration: 100           })
        }).then(res=>res.json())
            .then(data=> {
                console.log(data);
            });


    }

    function showMissionDetails() {
        fetch(`/mission/1`, {
            method: "GET", headers: {
                "Authorization": `Bearer ${jwt}`
            }
        }).then(res=>res.json())
            .then(data=>dispatch({
                type: "mission",
                data: data
            }));
    }

    useEffect(() => {
        if (part && storage && resource) {
            setResourceMessage({
                part: part, type: 'shield upgrade', data: resource, storage: storage, id: ship.id
            });
            setShow(true);
        }
        setPart(null);
        setResource(null);
        setStorage(null)
    }, [displayResource])

    useEffect(() => {
        if (colorListDisplay && shipColors) {
            setListToDisplay(<div
                style={{display: "flex", flexFlow: "row", justifyContent: "space-between", width: "100%"}}>
                <select name="cars" defaultValue={color} onChange={(e) => {
                    setColor(e.target.value);
                }}>
                    {shipColors.map(colorM => <option key={colorM} value={colorM}>{colorM}</option>)}
                </select>
                <div className="button" onClick={changeColor}>Choose</div>
            </div>)
        }
    }, [colorListDisplay, shipColors])

    useEffect(() => {
        if (color !== ship.color && triggerColorChange) {
            fetch(`/ship/${ship.id}`, {
                method: "PATCH", headers: {
                    "Content-Type": "application/json", "Authorization": `Bearer ${jwt}`
                }, body: JSON.stringify({
                    color: color.toUpperCase()
                })
            });
        }
    }, [triggerColorChange, color])


    return (<>
        <div className="container" style={{display: "flex", flexFlow: "column"}}>
            <div className="ship-name row" style={{fontSize: "50px"}}>
                <div>{shipName}</div>
                {nameInputDisplay ? <></> : <div className="button" onClick={() => {
                    setNameInputDisplay(true);
                }}>Rename</div>}
            </div>
            {nameInputDisplay ? <div>{getShipName()}</div> : <></>}
            <div className="ship-type" style={{fontSize: "15px"}}>miner ship</div>
            <div className="ship-color row">
                {colorListDisplay ? listTodDisplay : <>
                    <div> Color: {color.toLowerCase()}</div>
                    <div className="button" onClick={async () => {
                        await getAvailableColors();
                        setColorListDisplay(true)
                    }}>Change
                    </div>
                </>}
            </div>
            <div className="ship-status row">
                <div>Status: {ship.status}</div>
                <div className="button" onClick={() => {
                    if (ship.status === "In dock") sendShipToMission();
                    else showMissionDetails();
                }}>{ship.status === "In dock" ? "Send" : "Show"}
                </div>
            </div>
            <div className="ship-shield row">
                <div>Shield | lvl: {ship.shieldLevel} | {ship.shieldEnergy} / {ship.maxShieldEnergy}</div>
                <div className="button" onClick={() => onClick("SHIELD")}>Upgrade
                </div>
            </div>
            <div className="ship-drill row">
                <div>Drill | lvl: {ship.drillLevel} | resource/hour: {ship.drillEfficiency}</div>
                <div className="button" onClick={() => onClick("DRILL")}> Upgrade
                < /div>
            </div>
            <div className="ship-engine row">
                <div>Engine | lvl: {ship.engineLevel} | lightyear/hour: {ship.maxSpeed}</div>
                <div className="button" onClick={() => onClick("ENGINE")}>Upgrade
                </div>
            </div>
            <div className="ship-storage row">
                <div>Storage | lvl: {ship.storageLevel} | {ship.amounntOfCurrentlyStoredItems} / {ship.maxStorageCapacity}</div>
                <div className="button" onClick={() => onClick("STORAGE")}>Upgrade
                </div>

            </div>
            <div className="ship-storage-contents" style={{display: "flex", flexFlow: "column", alignItems: "flex-start", height: "min-content" }}>
                {ship.resources.length > 0
                    // ? console.log(ship.resources)
                    ? ship.resources.map((resource) => {
                        console.log(resource);
                        return (<div className="resource-row" key={resource.resourceType}>
                            <img style={{width: "25px", height: "25px"}} src={(resource.resourceType).toLowerCase() + '.png'}
                                 alt={resource.resourceType}/>
                            <div style={{marginLeft: "5px"}} key={resource.resourceType}>{resource.resourceType}: {resource.quantity}</div>
                        </div>);
                    })
                    : <div>Ship storage is empty</div>}
            </div>
            <div style={{height: "min-content", paddingTop: "15px"}}>
                {show ? <ResourceNeeded message={resourceMessage} checkStorage={checkStorage}/> : <></>}
            </div>
        </div>
        <div className="ship-img">
            <img src="ship_five.png" style={{
                width: "128px", height: "128px", padding: "60px", justifySelf: "center", alignSelf: "center"
            }}/>
        </div>

    </>)
}

