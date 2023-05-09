import "./DisplayMinerShip.css";
import {ResourceNeeded} from "./ResourceNeeded";
import {useEffect, useState} from "react";

export function DisplayMinerShip({message, checkStorage}) {
    const ship = message.data;
    const [displayResource,setDisplayResource] = useState(false);
    let resource;
    let storage;
    const [resourceMessage,setResourceMessage] = useState(null);
    const [show, setShow] = useState(false);



    async function getShipPartUpgradeCost(part) {
        await fetch(`http://localhost:8080/ship/miner/${ship.id}/upgrade?part=${part}`)
            .then(res => res.json())
            .then(data => {
                resource = data;
            })
            .catch(err => console.error(err));
    }

    function getStorage() {
        fetch("http://localhost:8080/base/1/storage/resources")
            .then(res => res.json())
            .then(data => {
                storage = data;
            })
            .catch(err => console.error(err));
    }

    async function onClick(part) {
        await getStorage();
        await getShipPartUpgradeCost(part);
        setResourceMessage({
            part: part,
            type: 'shield upgrade',
            data: resource,
            storage: storage,
            id: ship.id
        })
        setDisplayResource(!displayResource);
    }

    useEffect(() => {
        if (resourceMessage) {
            console.log(resourceMessage);
            setShow(true);
        }
    }, [displayResource])


    return (<>
        <div className="container" style={{display: "flex", flexFlow: "column"}}>
            <div className="ship-name row" style={{fontSize: "50px"}}>
                <div>{ship.name}</div>
                <div className="button">Rename</div>
            </div>
            <div className="ship-type" style={{fontSize: "15px"}}>miner ship</div>
            <div className="ship-color row">
                <div> Color: {ship.color.toLowerCase()} </div>
                <div className="button">Change</div>
            </div>
            <div className="ship-status">Status: {ship.status}</div>
            <div className="ship-shield row">
                <div>Shield | lvl: {ship.shieldLevel} | {ship.shieldEnergy} / {ship.maxShieldEnergy}</div>
                <div className="button" onClick={async () => {
                    await onClick("SHIELD");
                }
                }>Upgrade
                </div>
            </div>
            <div className="ship-drill row">
                <div>Drill | lvl: {ship.drillLevel} | resource/hour: {ship.drillEfficiency}</div>
                <div className="button"
                >Upgrade
                </div>
            </div>
            <div className="ship-engine row">
                <div>Engine | lvl: {ship.engineLevel} | lightyear/hour: {ship.maxSpeed}</div>
                <div className="button"
                >Upgrade
                </div>
            </div>
            <div className="ship-storage row">
                <div>Storage | lvl: {ship.storageLevel} | {ship.resources.length} / {ship.maxStorageCapacity}</div>
                <div className="button"
                >Upgrade
                </div>
            </div>
        </div>
        <div className="ship-img">
            <img src="ship_five.png" style={{
                width: "128px",
                height: "128px",
                padding: "60px",
                justifySelf: "center",
                alignSelf: "center"
            }}/>
        </div>
        <div>
            {show ? <ResourceNeeded message={resourceMessage} checkStorage={checkStorage}/> : <></>}
        </div>
    </>)
}

