import "./DisplayMinerShip.css";
import {ResourceNeeded} from "./ResourceNeeded";
import {useEffect, useState} from "react";
import {useOutletContext, useParams} from "react-router-dom";
import {useMessageDispatchContext} from "../MessageContext";
import {ShipName} from "./ShipName";
import {ShipColor} from "./ShipColor";
import {ShipStatus} from "./ShipStatus";
import {useStorageDispatchContext} from "../StorageContext";

export default function DisplayMinerShip({message, checkStorage}) {
    const dispatch = useMessageDispatchContext();
    const {stationId} = useOutletContext();
    const {id} = useParams();
    const [ship, setShip] = useState(null);
    const [cost, setCost] = useState(null);
    const storageSetter = useStorageDispatchContext();
    const [storage, setStorage] = useState(null);
    //const [displayResource, setDisplayResource] = useState(false);
    const [resourceMessage, setResourceMessage] = useState(null);
    const [show, setShow] = useState(false);
    const [part, setPart] = useState(null);


    useEffect(() => {
        fetch(`/api/v1/ship/miner/${id}`)
            .then((res) => res.json())
            .then((data) => {
                console.log(data);
                setShip(data);
            });
    }, [id]);

    async function getShipPartUpgradeCost(part) {
        await fetch(`/api/v1/ship/miner/${ship.id}/upgrade?part=${part}`)
            .then((res) => res.json())
            .then((data) => setCost(data))
            .catch((err) => console.error(err));
    }

    // function getStorage() {
    //     fetch(`/api/v1/base/${stationId}/storage/resources`)
    //         .then((res) => res.json())
    //         .then((data) => setStorage(data))
    //         .catch((err) => console.error(err));
    // }

    async function onClick(part) {
        //await getStorage();
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

    // useEffect(() => {
    //     if (part && cost) {
    //         setResourceMessage({
    //             part: part, type: part.toLowerCase() + " upgrade", data: cost, storage: storage, id: ship.id,
    //         });
    //         setShow(true);
    //     }
    // }, [displayResource]);

    if (ship == null) {
        return <div>Loading...</div>;
    }

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
                <div>Engine | lvl: {ship.engineLevel} | lightyear/hour: {ship.maxSpeed}</div>
                <div className="button" onClick={() => onClick("ENGINE")}>Upgrade</div>
            </div>
            <div className="ship-storage row">
                <div> Storage |
                    lvl: {ship.storageLevel} | {ship.amountOfCurrentlyStoredItems} / {ship.maxStorageCapacity}</div>
                <div className="button" onClick={() => onClick("STORAGE")}>Upgrade</div>
            </div>
            <div className="ship-storage-contents"
                 style={{display: "flex", flexFlow: "column", alignItems: "flex-start", height: "min-content",}}>
                {ship.resources.length > 0 ? (ship.resources.map((resource) => {
                    return (<div className="resource-row" key={resource.resourceType}>
                        <img
                            style={{width: "25px", height: "25px"}}
                            src={"/" + resource.resourceType.toLowerCase() + ".png"}
                            alt={resource.resourceType}
                        />
                        <div
                            style={{marginLeft: "5px"}}
                            key={resource.resourceType}
                        >
                            {resource.resourceType}: {resource.quantity}
                        </div>
                    </div>);
                })) : (<div>Ship storage is empty</div>)}
            </div>

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
