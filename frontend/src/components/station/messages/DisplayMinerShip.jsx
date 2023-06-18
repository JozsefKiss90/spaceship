import "./DisplayMinerShip.css";
import {ResourceNeeded} from "./ResourceNeeded";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";import {ShipName} from "./ShipName";
import {ShipColor} from "./ShipColor";
import {ShipStatus} from "./ShipStatus";
import {useStorageDispatchContext} from "../StorageContext";
import ShipResources from "./ShipResources/ShipResources";

export default function DisplayMinerShip() {
    const {id} = useParams();
    const [ship, setShip] = useState(null);
    const [cost, setCost] = useState(null);
    const storageSetter = useStorageDispatchContext();
    const [part, setPart] = useState(null);

    useEffect(() => {
        fetch(`/api/v1/ship/miner/${id}`)
            .then((res) => res.json())
            .then((data) => {
                setShip(data);
            });
    }, [id]);

    useEffect(() => {
        setPart(null);
    }, [id]);

    async function getShipPartUpgradeCost(part) {
        await fetch(`/api/v1/ship/miner/${ship.id}/upgrade?part=${part}`)
            .then((res) => res.json())
            .then((data) => setCost(data))
            .catch((err) => console.error(err));
    }

    async function onClick(part) {
        await getShipPartUpgradeCost(part);
        setPart(part);
    }

    const upgradePart = async () => {
        await fetch(`/api/v1/ship/miner/${ship.id}/upgrade?part=${part}`, {
            method: "PATCH"
        })
            .then(res => {
                if (res.ok) {
                    return res.json();
                }
            })
            .then(data => {
                setShip(data);
                setPart(null);
                setCost(null);
                storageSetter({type: "update"});
            })
            .catch(err => console.log(err));
    }

    if (ship == null) {
        return <div>Loading...</div>;
    }

    const resourceSum = Object.values(ship.resources).reduce((sum, next) => sum + next, 0);

    return (<>
        <div
            className="container"
            style={{display: "flex", flexFlow: "column"}}
        >
            <ShipName ship={ship}/>
            <div className="ship-type" style={{fontSize: "13px", height: "15px"}}>
                miner ship
            </div>
            <ShipColor ship={ship}/>
            <ShipStatus ship={ship}/>
            <div className="ship-shield row">
                <div> Shield | lvl: {ship.shieldLevel} | {ship.shieldEnergy} / {ship.maxShieldEnergy}</div>
                <div className="button" onClick={() => onClick("SHIELD")}>Upgrade</div>
            </div>
            <div className="ship-drill row">
                <div>Drill | lvl: {ship.drillLevel} | resource/hour: {ship.drillEfficiency}</div>
                <div className="button" onClick={() => onClick("DRILL")}>Upgrade</div>
            </div>
            <div className="ship-engine row">
                <div>Engine | lvl: {ship.engineLevel} | AU/hour: {ship.maxSpeed}</div>
                <div className="button" onClick={() => onClick("ENGINE")}>Upgrade</div>
            </div>
            <div className="ship-storage row">
                <div> Storage |
                    lvl: {ship.storageLevel} | {resourceSum} / {ship.maxStorageCapacity}</div>
                <div className="button" onClick={() => onClick("STORAGE")}>Upgrade</div>
            </div>
            <ShipResources ship={ship} setShip={setShip}/>
        </div>
        <div className="side-bar">
            <img
                alt="spaceship"
                src="/ship_five.png"
                style={{
                    width: "128px", height: "128px", padding: "60px", justifySelf: "end", alignSelf: "end",
                }}
            />
            <div className="resource-needed" style={{height: "min-content", paddingTop: "15px"}}>
                {part ? (<ResourceNeeded
                    cost={cost}
                    item={part.toLowerCase()}
                    onConfirm={upgradePart}
                />) : (<></>)}
            </div>
        </div>
    </>);
}
